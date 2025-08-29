uniform sampler2D DiffuseSampler0;
uniform sampler2D DiffuseDepthSampler;

in vec4 vertexColor;

out vec4 fragColor;

void main() {
    fragColor = vertexColor;
}