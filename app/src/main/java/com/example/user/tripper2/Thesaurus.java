package com.example.user.tripper2;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import net.jeremybrooks.knicker.AccountApi;
import net.jeremybrooks.knicker.Knicker;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.dto.Related;
import net.jeremybrooks.knicker.dto.TokenStatus;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class Thesaurus {

    private static final String[] noun_tags = {"NN","NNS","NNP","NNPS","."};
    private static final String[] positive_verbs = {"love","prefer","would","do","like"};
    private static final String[] negative_verbs = {"hate","refuse","dont"};
    private static final String[] verb_tags = {"VB","VBD","VBG","VBP","VBN","VBZ"};
    private static final String[] cc_tags = {"but","not"};
    private static Context context;

    public static ArrayList<String> calculateInterestedWords(Context contextt,ArrayList<String> words) throws Exception
    {
        context = contextt;
        ArrayList<Integer> cost = new ArrayList<Integer>();
        ArrayList<String> interested_words = new ArrayList<String>();
        int j = 0, value = 1; // iterate over cost
        String verb = null;
        String connector = null;
        List<String> relatives = new ArrayList<String>();

        String sentence = "";

        for (String word : words)
        {
            sentence += word + " ";
        }

        String[] tags = POSTag(sentence);

        Boolean negative_verb = false, negative_cc = false,update,proceed;

        for(int i = 0; i < tags.length; ++i)
        {
            if(Arrays.asList(noun_tags).contains(tags[i]))
                cost.add(i + 1);
        }

        for(int i = 0; i < tags.length; ++i)
        {
            update = false;
            proceed = false;
            if(Arrays.asList(verb_tags).contains(tags[i]) || words.get(i).equals("like"))
            {
                update = true;
                proceed = true;
                verb = words.get(i);
                relatives = getRelativesByWord(verb,"antonym");

                negative_verb = Arrays.asList(negative_verbs).contains(verb);
                if((!relatives.isEmpty() && retainCommonWithBase(relatives,positive_verbs)) || negative_verb)
                {
                    if(cost.get(j) > 0)
                        value = -1;
                    else
                        value = 1;
                }
                else
                {
                    if (cost.get(j) < 0)
                        value = -1;
                    else
                        value = 1;
                }
            }
            else if(tags[i].equals("CC"))
            {

                relatives = getRelativesByWord(tags[i],"synonym");
                negative_cc = Arrays.asList(cc_tags).contains(words.get(i));
                if((!relatives.isEmpty() && retainCommonWithBase(relatives,cc_tags)) || negative_cc)
                {
                    value = -1;
                    update = true;
                }
            }

            for(int temp = j; temp < cost.size() && update; ++temp)
                if(Math.abs(cost.get(j)) - 1>= i)
                    cost.set(temp, value * cost.get(temp));
            if(update && proceed)
                ++j;
        }

        for(int i : cost)
        {
            if(i > 0)
            {
                interested_words.add(words.get(i - 1));
            }
        }

        return interested_words;
    }

    private static List<String> getRelativesByWord(String word, String type) throws Exception
    {
        List<String> relativeWords = new ArrayList<String>();


        List<Related> list;
        if(type.equals("synonym"))
            list = WordApi.related(word, false, EnumSet.of(Knicker.RelationshipType.synonym),100);
        else
            list = WordApi.related(word, false, EnumSet.of(Knicker.RelationshipType.antonym),100);


        for (Related d : list) {
            relativeWords.addAll(d.getWords());
        }

        return relativeWords;
    }

    private static Boolean retainCommonWithBase(List<String> set1, String[] set2) {
        List<String> result = new ArrayList<String>(set1);
        result.retainAll(Arrays.asList(set2));
        if(result.isEmpty())
            return false;
        return true;
    }

    public static String[] POSTag(String sentence) throws IOException {
       // POSModel model = new POSModelLoader().load(byteRead("en-pos-maxent.bin"));
        //InputStream input = Global.keywords.input;
        POSTaggerME tagger = Global.keywords.tagger;
        Log.d("Amit","Model: ");
        //POSModel model = new POSModel(input);
        //Log.d("Amit","Model: " + tagger.toString());
        PerformanceMonitor perfMon = new PerformanceMonitor(System.err, "sent");
        Log.d("Amit","Model: " + perfMon.toString());
        //POSTaggerME tagger = new POSTaggerME(model);
        Log.d("Amit","Model: " + tagger.toString());
        String[] test = null;

        ObjectStream<String> lineStream = new PlainTextByLineStream(
                new StringReader(sentence));

        perfMon.start();
        String line;
        while ((line = lineStream.read()) != null) {

            String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
                    .tokenize(line);
            String[] tags = tagger.tag(whitespaceTokenizerLine);

            POSSample sample = new POSSample(whitespaceTokenizerLine, tags);
            System.out.println(sample.toString());
            test = sample.getTags();

            perfMon.incrementCounter();
        }
        return test;
    }
    public static File byteRead(String aInputFileName) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream input = null;
        try {
            Log.d("Amit","HEY");
            input = context.getResources().getAssets().open(aInputFileName);
            Log.d("Amit",input.toString() + " : " + input.equals(null));
            try {
                byte[] buffer = new byte[1024];
                int read;
              while ((read = input.read(buffer)) != -1) {
                    baos.write(buffer, 0, read);
                }
                Log.d("Amit","RETURN");
                //return input;

            } finally {
                input.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            Log.d("Amit", ex.getMessage() + "1");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.d("Amit", ex.getMessage() + "2");
        }
        File tempFile = null;
        Log.d("Home", "Total No of bytes : " + baos.size());
        try {
              tempFile = File.createTempFile("en-pos-maxent", ".bin",  context.getCacheDir());
        }
        catch (IOException e)
        {
            e.getStackTrace();
        }
        try {
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(baos.toByteArray());
        }
        catch (FileNotFoundException e)
        {
            e.getStackTrace();
        }
        catch (IOException e)
        {
            e.getStackTrace();
        }

        return tempFile;
    }
} // end of Thesaurus
