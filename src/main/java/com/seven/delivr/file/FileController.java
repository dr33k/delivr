package com.seven.delivr.file;

import com.seven.delivr.enums.PublicEnum;
import com.seven.delivr.responses.Response;
import com.seven.delivr.util.Constants;
import com.seven.delivr.util.Responder;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(Constants.VERSION+"/file")
@SecurityRequirement(name="jwtAuth")
@Slf4j
public class FileController {
    public final FileService fileService;
    public FileController(FileService fileService){
        this.fileService = fileService;
    }
    @PreAuthorize("hasAnyRole('VEND_ADMIN', 'VENDOR')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseEntity<Response> upload(@Valid @NotEmpty @Size(min = 1, max = 6, message = "Min:1, Max:6") @RequestBody List<MultipartFile> files,
                                           @RequestParam("type") PublicEnum.UploadType uploadType){
            if (uploadType == PublicEnum.UploadType.LOGO) {
                return Responder.ok(fileService.uploadLogo(files));
            } else if (uploadType == PublicEnum.UploadType.PRODUCTS) {
                return Responder.ok(fileService.uploadProductImages(files));
            }

        return null;
    }
    @PreAuthorize("hasAnyRole('VEND_ADMIN', 'VENDOR')")
    @PostMapping(value = "/delete")
    public ResponseEntity<Response> delete(@Valid @NotBlank @RequestParam String key){
        fileService.delete(key);
        return Responder.noContent();
    }
}
