import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TP3 {

	public static class TP3Mapper extends Mapper<Object, Text, Text, IntWritable>{

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

			JSONParser parser = new JSONParser();

			try {
				JSONObject json = (JSONObject) parser.parse(value.toString());
				Tweet tweet = new Tweet(json);

				List<String> tweet_list_hashtags = tweet.getTweet_list_hashtags();
				for (int i = 0; i < tweet_list_hashtags.size(); i++) {
					String hashtag = tweet_list_hashtags.get(i);
					context.write(new Text(hashtag), new IntWritable(1));
				}

			} catch (ParseException e) {
				e.printStackTrace();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public static class TP3Reducer extends Reducer<Text,IntWritable,Text,IntWritable> {

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for(IntWritable value : values){
				sum += value.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}



	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "TP3");
		job.setNumReduceTasks(1);
		job.setJarByClass(TP3.class);
		job.setMapperClass(TP3Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setReducerClass(TP3Reducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
