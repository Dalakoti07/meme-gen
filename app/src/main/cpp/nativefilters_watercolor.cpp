#include <jni.h>
#include <android/bitmap.h>
#include <vector>
#include <algorithm>

static inline unsigned char clamp_uc(int v) {
    if (v < 0) return 0;
    if (v > 255) return 255;
    return (unsigned char)v;
}

// Kuwahara filter (basic, square window). Produces watercolor-like smoothing.
extern "C"
JNIEXPORT void JNICALL
Java_com_dalakoti07_android_memegenerator_nativefilters_NativeFilters_applyWatercolor(
        JNIEnv* env, jobject /*thiz*/, jobject bitmap, jint radius) {
    if (bitmap == nullptr) return;
    AndroidBitmapInfo info{}; void* pixels = nullptr;
    if (AndroidBitmap_getInfo(env, bitmap, &info) != ANDROID_BITMAP_RESULT_SUCCESS) return;
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) return;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) return;

    const int w = (int)info.width;
    const int h = (int)info.height;
    const int stride = (int)info.stride;
    const int r = std::max(1, (int)radius);

    std::vector<unsigned char> src((size_t)h * stride);
    for (int y = 0; y < h; ++y) {
        unsigned char* row = (unsigned char*)pixels + (size_t)y * stride;
        std::copy(row, row + stride, src.data() + (size_t)y * stride);
    }

    for (int y = 0; y < h; ++y) {
        unsigned char* outRow = (unsigned char*)pixels + (size_t)y * stride;
        for (int x = 0; x < w; ++x) {
            // 4 subregions (quadrants) stats: mean and variance over RGB
            float bestVar = 1e30f; int bestR=0, bestG=0, bestB=0;
            for (int q = 0; q < 4; ++q) {
                int x0 = (q==0 || q==2) ? x - r : x; // left half for 0,2
                int x1 = (q==0 || q==2) ? x : x + r; // right half for 1,3
                int y0 = (q==0 || q==1) ? y - r : y; // top half for 0,1
                int y1 = (q==0 || q==1) ? y : y + r; // bottom half for 2,3
                x0 = std::max(0, x0); y0 = std::max(0, y0);
                x1 = std::min(w-1, x1); y1 = std::min(h-1, y1);
                int count = 0; double meanR=0, meanG=0, meanB=0; double var=0;
                for (int yy = y0; yy <= y1; ++yy) {
                    const unsigned char* inRow = src.data() + (size_t)yy * stride;
                    for (int xx = x0; xx <= x1; ++xx) {
                        const unsigned char* p = inRow + xx * 4;
                        meanR += p[0]; meanG += p[1]; meanB += p[2];
                        count++;
                    }
                }
                if (count == 0) continue;
                meanR /= count; meanG /= count; meanB /= count;
                for (int yy = y0; yy <= y1; ++yy) {
                    const unsigned char* inRow = src.data() + (size_t)yy * stride;
                    for (int xx = x0; xx <= x1; ++xx) {
                        const unsigned char* p = inRow + xx * 4;
                        float dr = p[0] - (float)meanR;
                        float dg = p[1] - (float)meanG;
                        float db = p[2] - (float)meanB;
                        var += dr*dr + dg*dg + db*db;
                    }
                }
                var /= count;
                if (var < bestVar) {
                    bestVar = var; bestR = (int)meanR; bestG = (int)meanG; bestB = (int)meanB;
                }
            }
            outRow[x*4 + 0] = clamp_uc(bestR);
            outRow[x*4 + 1] = clamp_uc(bestG);
            outRow[x*4 + 2] = clamp_uc(bestB);
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}


