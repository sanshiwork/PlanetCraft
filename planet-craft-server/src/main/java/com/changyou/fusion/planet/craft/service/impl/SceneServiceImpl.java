package com.changyou.fusion.planet.craft.service.impl;

import com.changyou.fusion.planet.craft.service.CacheService;
import com.changyou.fusion.planet.craft.service.SceneService;
import com.changyou.fusion.planet.craft.util.JSON;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CacheService cacheService;

    @PostConstruct
    public void init() throws IOException {
        // 从redis中获取数据，如果没有，则使用模板初始化
        String planet = cacheService.get(CacheService.PREFIX.PLANET.getValue() + "array");
        if (planet == null || planet.isEmpty()) {
            Resource resource = new ClassPathResource("static/init.json");
            faces = new Gson().fromJson(new InputStreamReader(resource.getInputStream()), int[].class);
        } else {
            faces = JSON.fromJson(planet, int[].class);
        }
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
