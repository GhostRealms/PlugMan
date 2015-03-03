package com.rylinaux.plugman.util;

/*
 * #%L
 * PlugMan
 * %%
 * Copyright (C) 2010 - 2015 PlugMan
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import com.google.common.io.ByteStreams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import org.json.JSONObject;

/**
 * Utilities for dealing with the Bukget API.
 *
 * @author rylinaux
 */
public class BukgetUtil {

    /**
     * The base URL for the Bukget API.
     */
    public static final String API_BASE_URL = "http://api.bukget.org/3/plugins/bukkit/";

    /**
     * Check if the given slug is a valid DBO plugin.
     *
     * @param slug the slug
     * @return true, if valid.
     */
    public static boolean isValidSlug(String slug) {

        HttpClient client = HttpClients.createMinimal();
        HttpGet get = new HttpGet(API_BASE_URL + slug + "?size=1");

        boolean found = false;

        try {

            HttpResponse response = client.execute(get);
            String body = IOUtils.toString(response.getEntity().getContent());

            JSONObject json = new JSONObject(body);

            found = !json.has("error");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;

    }

    public static JSONObject getPluginData(String slug) {
        return getPluginData(slug, "latest");
    }

    public static JSONObject getPluginData(String slug, String version) {

        HttpClient client = HttpClients.createMinimal();
        HttpGet get = new HttpGet(API_BASE_URL + slug + "/" + version);

        try {

            HttpResponse response = client.execute(get);
            String body = IOUtils.toString(response.getEntity().getContent());

            JSONObject json = new JSONObject(body);
            System.out.println(json.toString(2));

            return new JSONObject(body);

        } catch (IOException e) {

        }

        return null;

    }

    public static void downloadPlugin(String pluginName, String downloadUrl) {

        HttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(downloadUrl);

        try {

            HttpResponse response = client.execute(get);

            FileOutputStream output = new FileOutputStream(new File("plugins" + File.separator + pluginName + ".jar"));

            ByteStreams.copy(response.getEntity().getContent(), output);

            output.close();

        } catch (IOException e) {

        }

    }

}
