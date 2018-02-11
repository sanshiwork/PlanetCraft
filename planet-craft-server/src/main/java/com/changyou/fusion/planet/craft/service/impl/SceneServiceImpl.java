package com.changyou.fusion.planet.craft.service.impl;

import com.changyou.fusion.planet.craft.service.SceneService;
import com.google.gson.Gson;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * SceneService
 * <p>
 * Created by zhanglei_js on 2018/2/8.
 */
@Service
public class SceneServiceImpl implements SceneService {


    private int[] faces = new int[262144];

    @PostConstruct
    public void init() throws IOException {
        Resource resource = new ClassPathResource("static/init.json");
        faces = new Gson().fromJson(new InputStreamReader(resource.getInputStream()), int[].class);
    }

    @Override
    public int[] getPlanetFaces() {
        return faces;
    }

    @Override
    public void updatePlanetFace(int index, int color) {
        faces[index] = color;
    }
}
