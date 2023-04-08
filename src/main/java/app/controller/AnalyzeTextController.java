package app.controller;

import app.service.AwsTextractService;
import app.data.ProductData;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class AnalyzeTextController {

    private final AwsTextractService awsTextractService;

    @GetMapping(path = "/products/bad")
    public ResponseEntity<List<ProductData>> getBadProducts(
            @RequestParam("fileName") String fileName,
            @RequestParam("dietBadProducts") List<String> dietBadProducts) {
        return ResponseEntity.status(HttpStatus.OK).body(awsTextractService.findText(fileName, dietBadProducts));
    }

    @PostMapping("/")
    public ResponseEntity healthPost() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/")
    public ResponseEntity health() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
