uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;
uniform sampler2D CongealedVoidSampler;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    fragColor = texture(DiffuseSampler0, texCoord);

    vec4 voidColor = texture(CongealedVoidSampler, texCoord);

    if (voidColor.a > 0) {
        fragColor += voidColor;
    }
}