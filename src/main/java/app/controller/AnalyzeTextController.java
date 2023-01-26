package app.controller;

import app.AwsTextractService;
import app.client.AwsClient;
import app.data.ProductData;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@AllArgsConstructor
public class AnalyzeTextController {

    private final AwsTextractService awsTextractService;

    @GetMapping(path = "/products/bad")
    public ResponseEntity<List<ProductData>> getBadProducts(
            @RequestParam("file") MultipartFile file,
            @RequestParam("dietBadProducts") List<String> dietBadProducts) {
        return ResponseEntity.status(HttpStatus.OK).body(awsTextractService.findText(file, dietBadProducts));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
