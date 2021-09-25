package com.yk.player.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.yk.player.data.bean.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalMediaUtils {

    public static List<Video> getAllVideo(Context context) {
        List<Video> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, null, null,
                MediaStore.Video.Media.DATE_ADDED + " desc"
        );
        if (cursor == null) {
            return list;
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            long createTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
            long updateTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));

            Video video = new Video(name, path, duration, createTime * 1000, updateTime * 1000);

            list.add(video);
        }

        cursor.close();

        return list;
    }

    public static Map<String, List<Video>> getVideoCategory(List<Video> list) {
        Map<String, List<Video>> map = new HashMap<>();

        if (list == null || list.isEmpty()) {
            return map;
        }

        for (Video video : list) {
            String parentPath = getParentPath(video);
            if (TextUtils.isEmpty(parentPath)) {
                continue;
            }

            if (!map.containsKey(parentPath)) {
                List<Video> videoList = new ArrayList<>();
                videoList.add(video);
                map.put(parentPath, videoList);
            } else {
                List<Video> videoList = map.get(parentPath);
                if (videoList == null) {
                    videoList = new ArrayList<>();
                    videoList.add(video);
                    map.put(parentPath, videoList);
                } else {
                    videoList.add(video);
                }
            }
        }

        return map;
    }

    public static String getParentPath(Video video) {
        if (video == null) {
            return null;
        }

        String path = video.getPath();
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        File parent = file.getParentFile();
        if (parent == null) {
            return null;
        }

        return parent.getName();
    }
}
