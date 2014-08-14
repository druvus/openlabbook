////////////////////////////////////////////////////////
//
// Pipeline stage definitions for RNAseq pipeline 
// See prepere_data.groovy for more information.
// 
////////////////////////////////////////////////////////

prepere_sortmerna_5s = {
   doc "Creating binary database 5s"  
   exec "$SORTMERNADIR/buildtrie --db $db5s"
}

prepere_sortmerna_58s = {
   doc "Creating binary database 58s"  
   exec "$SORTMERNADIR/buildtrie --db $db58s"
}

prepere_sortmerna_16s = {
   doc "Creating binary database 16s"  
   exec "$SORTMERNADIR/buildtrie --db $db16s"
}

prepere_sortmerna_18s = {
   doc "Creating binary database 18s"  
   exec "$SORTMERNADIR/buildtrie --db $db18s"
}

prepere_sortmerna_23s = {
   doc "Creating binary database 23s"  
   exec "$SORTMERNADIR/buildtrie --db $db23s"
}


sra2fastq = {
    produce(input.prefix+"_1.fastq",input.prefix+"_2.fastq") {
        exec """
            fastq-dump --split-3 $input.sra
        """
    }
}

prepere_sortmerna_db = segment {
 [ prepere_sortmerna_5s + prepere_sortmerna_58s + prepere_sortmerna_16s + prepere_sortmerna_18s + prepere_sortmerna_23s ]
}

merge_readpair = {
   doc "Merge read-pairs"  
   exec """
   $SORTMERNADIR/scripts/merge-paired-reads.sh $input1.fq $input2.fq $output.fastq
   """
}


remove_rrna = {
   doc "Remove rRNA from reads"
     produce(input.prefix+"_accepted.fastq", input.prefix+"_rejected.fastq", input.prefix+"_sortmerna.log"){ 
	   exec """
		   $SORTMERNADIR/sortmerna 
		   -n $dbNum 
		   --db $dbs 
		   --I $input.fq 
		   --other $output2 
		   --log $output3 
		   -a $threads
		   -v 
		   --paired-in 
		   --bydbs 
		   --accept $output1
	   """
     }
}

unmerge_readpair = {
   doc "Unmerge read-pairs"  
   produce(input.prefix+"_norRNA_1.fastq", input.prefix+"_norRNA_2.fastq"){ 
      exec """
         $SORTMERNADIR/scripts/unmerge-paired-reads.sh $input $output1 $output2
      """
   }
}


trimmomatic = {
    doc "Trim reads using Trimmomatic"  
    filter("p1","u1","p2","u2") {
        exec """
            java -jar $TRIMMOMATIC/trimmomatic-0.32.jar PE 
            -threads $threads 
            -phred33 
            $input1.fastq $input2.fastq $output1 $output2 $output3 $output4 
            ILLUMINACLIP:adapter.fa:1:30:9 SLIDINGWINDOW:7:20 MINLEN:50
            //LEADING:20 TRAILING:20
        """
    }
}

