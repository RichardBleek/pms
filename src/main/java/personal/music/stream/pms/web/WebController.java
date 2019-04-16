package personal.music.stream.pms.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {

	@GetMapping("/jaydee/player")
	public String player() {
		return "index";
	}

	@GetMapping("/jaydee/player/{mix_id}")
	public String player(@PathVariable String mix_id, Model model) {
		model.addAttribute("mix_id", mix_id);
		return "index";
	}

}
