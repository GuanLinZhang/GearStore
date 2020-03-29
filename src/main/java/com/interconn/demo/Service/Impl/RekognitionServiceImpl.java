package com.interconn.demo.Service.Impl;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.interconn.demo.Service.RekognitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;

@Service
@Slf4j
public class RekognitionServiceImpl implements RekognitionService {
    private AmazonRekognition rekognitionClient;

    @Autowired
    public RekognitionServiceImpl(Region awsRegion) {
        this.rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion(Regions.fromName(awsRegion.getName())).build();
    }

    @Override
    public DetectLabelsResult detectLabels(MultipartFile file) throws IOException {
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(file.getBytes())))
                .withMaxLabels(10);
        return rekognitionClient.detectLabels(request);
    }
}
