package com.interconn.demo.Service;

import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RekognitionService {
    DetectLabelsResult detectLabels(MultipartFile file) throws IOException;
}
