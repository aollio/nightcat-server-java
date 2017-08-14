package com.nightcat.notice.service;

import com.nightcat.common.CatException;
import com.nightcat.entity.Notice;
import com.nightcat.entity.vo.NoticeVo;
import com.nightcat.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static com.nightcat.utility.Util.now;
import static com.nightcat.utility.Util.uuid;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    public NoticeSender sender() {
        return new NoticeSender(noticeRepository);
    }

    public List<Notice> findByUid(String uid) {
        return noticeRepository.findByUid(uid);
    }


    public NoticeVo toVo(Notice notice) {
        return NoticeVo.from(notice);
    }

    public List<NoticeVo> toVo(List<Notice> result) {
        List<NoticeVo> vos = new LinkedList<>();
        result.forEach(e -> vos.add(NoticeVo.from(e)));
        return vos;
    }


    public static class NoticeSender {

        private String id = uuid();
        private String content;
        private String uid;
        private Timestamp create_time = now();
        private boolean read = false;
        private Notice.Type type;
        private boolean del = false;

        private NoticeRepository noticeRep;

        NoticeSender(NoticeRepository noticeRep) {
            this.noticeRep = noticeRep;
        }

        public NoticeSender content(String content) {
            this.content = content;
            return this;
        }

        public NoticeSender uid(String uid) {
            this.uid = uid;
            return this;
        }


        public NoticeSender type(Notice.Type type) {
            this.type = type;
            return this;
        }

        public void send() {

            if (uid == null) {
                throw new CatException("NoticeSender uid must be exist");
            }

            if (type == null) {
                throw new CatException("NoticeSender type must be exist");
            }

            if (content == null) {
                throw new CatException("NoticeSender content must be exist");
            }

            Notice notice = new Notice(id, content, uid,
                    create_time, read, type, del);

            this.noticeRep.save(notice);
        }
    }


}