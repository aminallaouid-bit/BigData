// package spark.batch.tp21;

// import org.apache.spark.SparkConf;
// import org.apache.spark.api.java.JavaPairRDD;
// import org.apache.spark.api.java.JavaRDD;
// import org.apache.spark.api.java.JavaSparkContext;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import scala.Tuple2;
// import java.util.Arrays;
// import com.google.common.base.Preconditions;

// public class WordCountTask {

//     private static final Logger LOGGER = LoggerFactory.getLogger(WordCountTask.class);

//     public static void main(String[] args) {
//         // التحقق من إدخال مسار الملف ومسار الإخراج
//         Preconditions.checkArgument(args.length > 1, "Please provide the path of input file and output dir as parameters.");
//         new WordCountTask().run(args[0], args[1]);
//     }

// public void run(String inputFilePath, String outputDir) {
//     // إعداد Spark
//     // String master = "local[*]";
//     SparkConf conf = new SparkConf()
//             .setAppName(WordCountTask.class.getName())
//             .setMaster("spark://192.168.1.100:7077") // استبدل 192.168.1.100 بـ IP master
//     // ← أضف هذا السطر قبل إنشاء SparkContext
//     System.setProperty("hadoop.home.dir", "C:\\hadoop"); // ضع مسار مجلد فارغ أو فيه winutils.exe

//     JavaSparkContext sc = new JavaSparkContext(conf);

//     // قراءة الملف
//     JavaRDD<String> textFile = sc.textFile(inputFilePath);

//     // عد الكلمات
//     JavaPairRDD<String, Integer> counts = textFile
//             .flatMap(s -> Arrays.asList(s.split(" ")).iterator())
//             .mapToPair(word -> new Tuple2<>(word, 1))
//             .reduceByKey((a, b) -> a + b);

//   counts.collect().forEach(tuple -> 
//     System.out.println(tuple._1() + " : " + tuple._2())
// );
// }

// }

// *********************************
package spark.batch.tp21;

import java.util.Arrays;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class WordCountTask{
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: WordCountTask <input> <output>");
            System.exit(1);
        }
        String input = args[0];
        String output = args[1];

        SparkConf conf = new SparkConf()
                .setAppName(WordCountTask.class.getName());
                // لا نضبط master هنا إذا سنستخدم spark-submit مع --master

        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            JavaRDD<String> textFile = sc.textFile(input);

            JavaPairRDD<String, Integer> counts = textFile
                .flatMap(line -> Arrays.asList(line.split("\\s+")).iterator())
                .mapToPair(word -> new Tuple2<>(word.replaceAll("[^\\p{L}\\p{Nd}]+", ""), 1))
                .filter(t -> t._1() != null && !t._1().isEmpty())
                .reduceByKey(Integer::sum);

            counts.saveAsTextFile(output);
        }
    }
}


