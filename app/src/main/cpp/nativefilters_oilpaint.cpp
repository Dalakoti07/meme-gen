#include <jni.h>
#include <android/bitmap.h>
#include <vector>
#include <algorithm>
#include <cstdint>

static inline uint8_t clamp8(int v) { return (uint8_t) (v < 0 ? 0 : (v > 255 ? 255 : v)); }

// Simple oil-paint filter using neighborhood histogram (intensity bins)
extern "C"
JNIEXPORT void JNICALL
Java_com_dalakoti07_android_memegenerator_nativefilters_NativeFilters_applyOilPaint(
        JNIEnv* env, jobject /*thiz*/, jobject bitmap, jint radius, jint levels) {
    if (bitmap == nullptr) return;
    AndroidBitmapInfo info{}; void* pixels = nullptr;
    if (AndroidBitmap_getInfo(env, bitmap, &info) != ANDROID_BITMAP_RESULT_SUCCESS) return;
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) return;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) return;

    const int w = (int)info.width;
    const int h = (int)info.height;
    const int stride = (int)info.stride;
    const int bins = std::max(4, std::min(64, (int)levels));

    // copy source to a temp buffer
    std::vector<uint8_t> src((size_t)h * stride);
    for (int y = 0; y < h; ++y) {
        uint8_t* row = (uint8_t*)pixels + y * stride;
        std::copy(row, row + stride, src.data() + (size_t)y * stride);
    }

    std::vector<int> count(bins);
    std::vector<int> sumR(bins), sumG(bins), sumB(bins);

    auto intensityBin = [&](uint8_t r, uint8_t g, uint8_t b) {
        int intensity = (int)(0.299f*r + 0.587f*g + 0.114f*b);
        int bin = (int)((intensity * bins) / 256);
        if (bin >= bins) bin = bins - 1;
        if (bin < 0) bin = 0;
        return bin;
    };

    for (int y = 0; y < h; ++y) {
        uint8_t* outRow = (uint8_t*)pixels + y * stride;
        for (int x = 0; x < w; ++x) {
            std::fill(count.begin(), count.end(), 0);
            std::fill(sumR.begin(), sumR.end(), 0);
            std::fill(sumG.begin(), sumG.end(), 0);
            std::fill(sumB.begin(), sumB.end(), 0);

            int yl = std::max(0, y - radius);
            int yr = std::min(h - 1, y + radius);
            int xl = std::max(0, x - radius);
            int xr = std::min(w - 1, x + radius);

            for (int yy = yl; yy <= yr; ++yy) {
                const uint8_t* inRow = src.data() + (size_t)yy * stride;
                for (int xx = xl; xx <= xr; ++xx) {
                    const uint8_t* p = inRow + xx * 4;
                    int bin = intensityBin(p[0], p[1], p[2]);
                    count[bin]++;
                    sumR[bin] += p[0];
                    sumG[bin] += p[1];
                    sumB[bin] += p[2];
                }
            }

            int best = 0;
            for (int b = 1; b < bins; ++b) if (count[b] > count[best]) best = b;
            int c = std::max(1, count[best]);
            uint8_t r = (uint8_t)(sumR[best] / c);
            uint8_t g = (uint8_t)(sumG[best] / c);
            uint8_t b = (uint8_t)(sumB[best] / c);

            uint8_t* o = outRow + x * 4;
            o[0] = r; o[1] = g; o[2] = b; // keep o[3] alpha
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}


