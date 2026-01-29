package com.example.mayakantei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.mayakantei.service.MayaLogic;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class MayaController {

    private final MayaLogic mayaLogic = new MayaLogic();

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam("birthdate") String birthdateStr, Model model) {
        try {
            LocalDate birthdate = LocalDate.parse(birthdateStr);
            int kinNumber = mayaLogic.calculateKinNumber(birthdate);
            String kin = mayaLogic.calculateKin(birthdate);
            String description = mayaLogic.generateDescription(kinNumber);
            String color = mayaLogic.getSealColor(mayaLogic.getSolarSealName(kinNumber));

            // Generate simple SVG
            // Circle with colored fill and KIN text centered
            String svgText = "KIN " + kinNumber;
            String svg = String.format(
                    "<svg width=\"200\" height=\"200\" xmlns=\"http://www.w3.org/2000/svg\">" +
                            "<circle cx=\"100\" cy=\"100\" r=\"80\" fill=\"%s\" />" +
                            "<text x=\"50%%\" y=\"50%%\" dominant-baseline=\"middle\" text-anchor=\"middle\" font-size=\"24\" fill=\"#333\" font-family=\"Arial\">%s</text>"
                            +
                            "</svg>",
                    color, svgText);

            model.addAttribute("resultKin", kin);
            model.addAttribute("resultDescription", description);
            model.addAttribute("resultSvg", svg);
            model.addAttribute("birthdate", birthdate);
        } catch (Exception e) {
            model.addAttribute("error", "Invalid date format");
        }
        return "index";
    }
}
