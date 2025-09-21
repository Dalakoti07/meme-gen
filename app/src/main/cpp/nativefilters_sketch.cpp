#include <jni.h>
#include <android/bitmap.h>
#include <cmath>
#include <vector>
#include <algorithm>
#include <cstdint>

static inline uint8_t clamp8(int v) { return (uint8_t) (v < 0 ? 0 : (v > 255 ? 255 : v)); }

extern "C"
JNIEXPORT void JNICALL
Java_com_dalakoti07_android_memegenerator_nativefilters_NativeFilters_applySketch(
        JNIEnv* env, jobject /*thiz*/, jobject bitmap, jfloat edgeSigma, jfloat shadeSigma) {
    if (bitmap == nullptr) return;
    AndroidBitmapInfo info{}; void* pixels = nullptr;
    if (AndroidBitmap_getInfo(env, bitmap, &info) != ANDROID_BITMAP_RESULT_SUCCESS) return;
    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) return;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) != ANDROID_BITMAP_RESULT_SUCCESS) return;

    const int width = (int)info.width;
    const int height = (int)info.height;
    const int stride = (int)info.stride;

    std::vector<uint8_t> gray(width * height);
    for (int y = 0; y < height; ++y) {
        uint8_t* row = (uint8_t*)pixels + y * stride;
        for (int x = 0; x < width; ++x) {
            uint8_t* px = row + x * 4; // RGBA
            int r = px[0], g = px[1], b = px[2];
            gray[y * width + x] = (uint8_t)((0.299f*r + 0.587f*g + 0.114f*b));
        }
    }

    const int k = (int)std::max(1.f, shadeSigma);
    std::vector<uint8_t> blur1(width * height), blur2(width * height);
    for (int y = 0; y < height; ++y) {
        int idx = y * width;
        for (int x = 0; x < width; ++x) {
            int xl = std::max(0, x - k);
            int xr = std::min(width - 1, x + k);
            int s = 0;
            for (int xi = xl; xi <= xr; ++xi) s += gray[idx + xi];
            blur1[idx + x] = (uint8_t)(s / (xr - xl + 1));
        }
    }
    for (int x = 0; x < width; ++x) {
        for (int y = 0; y < height; ++y) {
            int yt = std::max(0, y - k);
            int yb = std::min(height - 1, y + k);
            int s = 0;
            for (int yi = yt; yi <= yb; ++yi) s += blur1[yi * width + x];
            blur2[y * width + x] = (uint8_t)(s / (yb - yt + 1));
        }
    }

    std::vector<uint8_t> edges(width * height);
    auto at = [&](int yy, int xx){
        yy = std::max(0, std::min(height-1, yy));
        xx = std::max(0, std::min(width-1, xx));
        return (int)gray[yy*width+xx];
    };
    for (int y = 0; y < height; ++y) {
        for (int x = 0; x < width; ++x) {
            int gx = -at(y-1,x-1) + at(y-1,x+1)
                     -2*at(y,x-1) + 2*at(y,x+1)
                     -at(y+1,x-1) + at(y+1,x+1);
            int gy = -at(y-1,x-1) -2*at(y-1,x) - at(y-1,x+1)
                     +at(y+1,x-1) +2*at(y+1,x) + at(y+1,x+1);
            int mag = std::min(255, (int)std::sqrt((float)(gx*gx + gy*gy)));
            edges[y*width + x] = (uint8_t)mag;
        }
    }

    for (int y = 0; y < height; ++y) {
        uint8_t* row = (uint8_t*)pixels + y * stride;
        for (int x = 0; x < width; ++x) {
            uint8_t shade = blur2[y*width + x];
            uint8_t edge = edges[y*width + x];
            int invShade = 255 - shade;
            int invEdge = 255 - edge;
            int res = 255 - (invShade * invEdge) / 255;
            row[x*4 + 0] = row[x*4 + 1] = row[x*4 + 2] = clamp8(res);
        }
    }

    AndroidBitmap_unlockPixels(env, bitmap);
}


