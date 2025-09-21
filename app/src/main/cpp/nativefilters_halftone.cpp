#include <jni.h>
#include <android/bitmap.h>
#include <algorithm>

// Monochrome comic halftone: per-cell dot with perceptual (area) scaling and simple gamma
extern "C"
JNIEXPORT void JNICALL
Java_com_dalakoti07_android_memegenerator_nativefilters_NativeFilters_applyHalftone(
        JNIEnv* env, jobject /*thiz*/, jobject bitmap, jint cellSize) {
    if (bitmap == nullptr) return;
    AndroidBitmapInfo info{}; void* pixels = nullptr;
    if (AndroidBitmap_getInfo(env, bitmap, &info) != ANDROID_BITMAP_RESULT_SUCCESS) return;
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) return;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) return;

    const int w = (int)info.width;
    const int h = (int)info.height;
    const int stride = (int)info.stride;
    const int cs = std::max(4, (int)cellSize);
    const int half = cs / 2;
    const float gamma = 0.9f; // tweak contrast of dots (1.0 = linear)

    for (int y0 = 0; y0 < h; y0 += cs) {
        for (int x0 = 0; x0 < w; x0 += cs) {
            // Compute average luminance in the cell
            int x1 = std::min(w, x0 + cs);
            int y1 = std::min(h, y0 + cs);
            int sum = 0; int cnt = 0;
            for (int y = y0; y < y1; ++y) {
                const unsigned char* row = (unsigned char*)pixels + (size_t)y * stride;
                for (int x = x0; x < x1; ++x) {
                    const unsigned char* p = row + x * 4;
                    int lum = (int)(0.299f*p[0] + 0.587f*p[1] + 0.114f*p[2]);
                    sum += lum; cnt++;
                }
            }
            if (cnt == 0) continue;
            int avg = sum / cnt; // 0..255
            // Map luminance to area (perceptual): radius ~ sqrt(1 - L)
            float lin = 1.f - (avg / 255.f); // darker -> larger
            // gamma to tune mid-tones
            float t = powf(lin, gamma);
            int radius = (int)(sqrtf(std::max(0.f, t)) * half);
            if (radius < 1 && lin > 0.04f) radius = 1; // ensure visibility for not-quite-white
            int cx = x0 + half;
            int cy = y0 + half;

            // Fill cell with white background (paper)
            for (int y = y0; y < y1; ++y) {
                unsigned char* row = (unsigned char*)pixels + (size_t)y * stride;
                for (int x = x0; x < x1; ++x) {
                    unsigned char* o = row + x * 4;
                    o[0] = 255; o[1] = 255; o[2] = 255; // keep alpha
                }
            }
            // Draw black circle at center with computed radius
            int r2 = radius * radius;
            for (int dy = -radius; dy <= radius; ++dy) {
                int yy = cy + dy; if (yy < y0 || yy >= y1) continue;
                unsigned char* row = (unsigned char*)pixels + (size_t)yy * stride;
                for (int dx = -radius; dx <= radius; ++dx) {
                    if (dx*dx + dy*dy > r2) continue;
                    int xx = cx + dx; if (xx < x0 || xx >= x1) continue;
                    unsigned char* o = row + xx * 4;
                    o[0] = 0; o[1] = 0; o[2] = 0; // black dot
                }
            }
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}


