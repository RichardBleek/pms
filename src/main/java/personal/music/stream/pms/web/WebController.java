package personal.music.stream.pms.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import personal.music.stream.pms.mix.Mix;
import personal.music.stream.pms.service.MixService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

	private MixService mixService;

	WebController(MixService mixService) {
		this.mixService = mixService;
	}

	@GetMapping("${pms.applicationPath}/player")
	public String player(Model model) {
		List<Mix> mixes = new ArrayList<>();
		mixService.mixStream().subscribe(mixes::add);
	    model.addAttribute("mixes", mixes);
		return "list";
	}

	@GetMapping("${pms.applicationPath}/player/{mix_id}")
	public String player(@PathVariable String mix_id, Model model) {
	    model.addAttribute("mix", mixService.mixMono(mix_id));
		return "player";
	}

}
