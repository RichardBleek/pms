package personal.music.stream.pms.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

	@RequestMapping("/home")
	public String home() {
		return "index";
	}

	@GetMapping("/player/{mix_id}")
	public String homeWithParam(@PathVariable String mix_id, Model model) {
		model.addAttribute("mix_id", mix_id);
		return "index";
	}

}
