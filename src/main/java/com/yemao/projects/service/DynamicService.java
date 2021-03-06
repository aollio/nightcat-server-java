package com.yemao.projects.service;

import com.yemao.common.CatException;
import com.yemao.projects.models.*;
import com.yemao.projects.repository.DynamicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.yemao.utility.Util.now;
import static com.yemao.utility.Util.uuid;

@Service
public class DynamicService {


    @Autowired
    private DynamicRepository dynamicRepository;

    public DynamicLogger logger() {
        return new DynamicLogger(dynamicRepository);
    }


    public static class DynamicLogger {


        private String proj_id;
        private String uid;
        private boolean publisher;
        private Dynamic.Type type;
        private String content;
        private String img_url;

        private DynamicRepository dynamicRepository;

        private DynamicLogger(DynamicRepository dynamicRepository) {
            this.dynamicRepository = dynamicRepository;
        }


        public DynamicLogger project(String proj_id) {
            this.proj_id = proj_id;
            return this;
        }

        public DynamicLogger user(String uid) {
            this.uid = uid;
            return this;
        }

        public DynamicLogger publisher(boolean publisher) {
            this.publisher = publisher;
            return this;
        }

        public DynamicLogger type(Dynamic.Type type) {
            this.type = type;
            return this;
        }

        public DynamicLogger content(String content) {
            this.content = content;
            return this;
        }

        public DynamicLogger img(String img_url) {
            this.img_url = img_url;
            return this;
        }


        public Dynamic log() {

            Dynamic dynamic = new Dynamic();
            dynamic.setId(uuid());
            dynamic.setCreate_time(now());

            if (proj_id == null) {
                throw new CatException("Build Project Dynamic proj_id must be exist");
            }

            if (uid == null) {
                throw new CatException("Build Project Dynamic uid must be exist");
            }

            if (type == null) {
                throw new CatException("Build Project Dynamic type must be exist");
            }

            dynamic.setProj_id(proj_id);
            dynamic.setUid(uid);
            dynamic.setPublisher(publisher);
            dynamic.setType(type);
            dynamic.setContent(content);
            dynamic.setImg_url(img_url);

            dynamicRepository.save(dynamic);
            return dynamic;
        }
    }



}
