#include <jni.h>
#include <android/bitmap.h>
#include <cmath>
#include <vector>
#include <algorithm>
#include <cstdint>

static inline uint8_t clamp8(int v) { return (uint8_t) (v < 0 ? 0 : (v > 255 ? 255 : v)); }

extern "C"
JNIEXPORT void JNICALL
Java_com_dalakoti07_android_memegenerator_nativefilters_NativeFilters_applyTint(
        JNIEnv* env, jobject /*thiz*/, jobject bitmap, jint argbColor, jfloat strength) {
    if (bitmap == nullptr) return;
    AndroidBitmapInfo info{}; void* pixels = nullptr;
    if (AndroidBitmap_getInfo(env, bitmap, &info) != ANDROID_BITMAP_RESULT_SUCCESS) return;
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) return;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) return;

    const int width = (int)info.width;
    const int height = (int)info.height;
    const int stride = (int)info.stride; // bytes per row

    const int aT = (argbColor >> 24) & 0xFF;
    const int rT = (argbColor >> 16) & 0xFF;
    const int gT = (argbColor >> 8) & 0xFF;
    const int bT = (argbColor) & 0xFF;

    const float alpha = fminf(fmaxf(strength, 0.0f), 1.0f) * (aT / 255.0f);

    for (int y = 0; y < height; ++y) {
        uint8_t* row = (uint8_t*)pixels + y * stride;
        for (int x = 0; x < width; ++x) {
            uint8_t* px = row + x * 4; // RGBA
            int r = px[0];
            int g = px[1];
            int b = px[2];
            int a = px[3];
            // simple lerp to tint color
            int nr = (int) (r + alpha * (rT - r));
            int ng = (int) (g + alpha * (gT - g));
            int nb = (int) (b + alpha * (bT - b));
            px[0] = clamp8(nr);
            px[1] = clamp8(ng);
            px[2] = clamp8(nb);
            px[3] = a; // keep original alpha
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}


