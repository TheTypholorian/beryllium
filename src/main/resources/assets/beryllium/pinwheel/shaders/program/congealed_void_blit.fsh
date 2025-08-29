#include "veil:fog"
#include "veil:space_helper"

uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D CongealedVoidSampler;
uniform sampler2D CongealedVoidDepthSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    fragColor = texture(DiffuseSampler0, texCoord);

    float voidDepth = texture(CongealedVoidDepthSampler, texCoord).r;
    float gameDepth = texture(DiffuseDepthSampler, texCoord).r;

    //if (voidDepth < gameDepth) {
        vec4 voidColor = texture(CongealedVoidSampler, texCoord);

        if (voidColor.a > 0) {
            vec3 pos = screenToLocalSpace(texCoord, gameDepth).xyz;
            float vertexDistance = fog_distance(pos, 0);
            fragColor = linear_fog(fragColor, vertexDistance, 8, 12, voidColor);
            //fragColor.rgb += (voidColor.rgb / voidColor.a) / vertexDistance;
        }
    //}
}