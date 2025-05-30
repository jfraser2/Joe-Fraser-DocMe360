package springboot.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateTemplate {
	@NotBlank(message = "The template text must not be blank")
	@Size(max = 200, message="Max Length is 200 characters")
	private String templateText;
	
	public CreateTemplate()
	{
	}
	
	public CreateTemplate(String templateText)
	{
		this.templateText = templateText;
	}

	public String getTemplateText() {
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}
	
}
