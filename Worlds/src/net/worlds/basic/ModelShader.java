package net.worlds.basic;

import org.wraith.engine.rendering.ShaderProgram;

public class ModelShader extends ShaderProgram{
	public ModelShader(){
		super(
			"#version 330\n"+"uniform mat4 uni_modelView;\nuniform mat4 uni_projection;\n"+"in vec3 att_pos;\n"+"\n"+"void main(){\n"
				+"	gl_Position = uni_projection*uni_modelView*vec4(att_pos, 1.0);\n"+"}",
			null, "#version 330\n"+"out vec4 fragColor;\n"+"\n"+"void main(){\n"+"	fragColor = vec4(1.0);\n"+"}");
		loadAttributes("att_position");
	}
}
