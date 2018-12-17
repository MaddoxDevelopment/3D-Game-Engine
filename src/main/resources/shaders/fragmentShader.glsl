#version 450
in vec2 passedTextureCoords;

out vec4 out_Color;

uniform sampler2D textureSampler;

void main(void){

	out_Color = texture(textureSampler, passedTextureCoords);

}