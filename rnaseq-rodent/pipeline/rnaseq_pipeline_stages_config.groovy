////////////////////////////////////////////////////////
//
// Pipeline stage definitions for RNAseq pipeline 
// See prepere_data.groovy for more information.
// 
////////////////////////////////////////////////////////
prepere_sortmerna_rfam_5s = {
   doc "Creating binary database rfam-5s"
   exec "$SORTMERNA/buildtrie --db ${db5s_rfam}"
}

prepere_sortmerna_rfam_58s = {
   doc "Creating binary database rfam-58s"
   exec "$SORTMERNA/buildtrie --db $db58s_rfam"
}

prepere_sortmerna_bac_16s = {
   doc "Creating binary database bac-16s"
   exec "$SORTMERNA/buildtrie --db $db16s_bac"
}

prepere_sortmerna_bac_23s = {
   doc "Creating binary database bac-23s"
   exec "$SORTMERNA/buildtrie --db $db23s_bac"
}


prepere_sortmerna_euk_18s = {
   doc "Creating binary database euk-18s"
   exec "$SORTMERNA/buildtrie --db $db18s_euk"
}

prepere_sortmerna_euk_28s = {
   doc "Creating binary database euk-28s"
   exec "$SORTMERNA/buildtrie --db $db28s_euk"
}

prepere_sortmerna_arc_16s = {
   doc "Creating binary database arc-16s"
   exec "$SORTMERNA/buildtrie --db $db16s_arc"
}

prepere_sortmerna_arc_23s = {
   doc "Creating binary database arc-23s"
   exec "$SORTMERNA/buildtrie --db $db23s_arc"
}

prepere_sortmerna_db = segment {
 [ prepere_sortmerna_rfam_5s + prepere_sortmerna_rfam_58s + prepere_sortmerna_bac_16s + prepere_sortmerna_bac_23s +  prepere_sortmerna_arc_16s + prepere_sortmerna_arc_23s + prepere_sortmerna_euk_18s + prepere_sortmerna_euk_28s ]
}

prepere_sortmerna_rfam_5s_v2 = {
   doc "Creating binary database rfam-5s"  
   exec "$SORTMERNA/indexdb_rna --ref $db5s_rfam"
}

prepere_sortmerna_rfam_58s_v2 = {
   doc "Creating binary database rfam-58s"  
   exec "$SORTMERNA/indexdb_rna --ref $db58s_rfam"
}

prepere_sortmerna_bac_16s_v2 = {
   doc "Creating binary database bac-16s"  
   exec "$SORTMERNA/indexdb_rna --ref $db16s_bac"
}

prepere_sortmerna_bac_23s_v2 = {
   doc "Creating binary database bac-23s"
   exec "$SORTMERNA/indexdb_rna --ref $db23s_bac"
}


prepere_sortmerna_euk_18s_v2 = {
   doc "Creating binary database euk-18s"  
   exec "$SORTMERNA/indexdb_rna --ref $db18s_euk"
}

prepere_sortmerna_euk_28s_v2 = {
   doc "Creating binary database euk-28s"  
   exec "$SORTMERNA/indexdb_rna --ref $db28s_euk"
}

prepere_sortmerna_arc_16s_v2 = {
   doc "Creating binary database arc-16s"
   exec "$SORTMERNA/indexdb_rna --ref $db16s_arc"
}

prepere_sortmerna_arc_23s_v2 = {
   doc "Creating binary database arc-23s"
   exec "$SORTMERNA/indexdb_rna --ref $db23s_arc"
}


prepere_sortmerna_db_v2 = segment {
 [ prepere_sortmerna_rfam_5s_v2 + prepere_sortmerna_rfam_58s_v2 + prepere_sortmerna_bac_16s_v2 + prepere_sortmerna_bac_23s_v2 +  prepere_sortmerna_bac_16s_v2 + prepere_sortmerna_arc_23s_v2 + prepere_sortmerna_euk_18s_v2 + prepere_sortmerna_euk_28s_v2 ]
}


sra2fastq = {
    produce(input.prefix+"_1.fastq",input.prefix+"_2.fastq") {
        exec """
            fastq-dump --split-3 $input.sra
        """
    }
}

merge_readpair = {
   doc "Merge read-pairs"  
   def output_name = "$input".split("/")[-1] // remove path to file
   output_name = output_name.prefix.prefix.split("_")[0] // only use common name for output    

   from("*.fq") produce("${output_name}.fastq") {
      exec """
         $SORTMERNA/scripts/merge-paired-reads.sh $input1.fq $input2.fq $output
      """
   }
}


remove_rrna = {
   doc "Remove rRNA from reads"
    produce(input.prefix+"_norrna"){
           exec """
                   $SORTMERNA/sortmerna
                   -n $dbNum 
                   --db $dbs
                   --I $input
                   --other $output
                   --log $input.prefix
                   -a $threads
                   -v
                   --paired-in
                   --bydbs
                   --accept $input.prefix
           """
     }
}


remove_rrna_v2 = {
   doc "Remove rRNA from reads"
     produce(input.prefix+"_accepted.fastq", input.prefix+"_rejected.fastq", input.prefix+"_sortmerna.log"){ 
	   exec """
		   $SORTMERNA/sortmerna 
		   --ref $dbs 
		   --reads $input 
		   --other $output2 
		   --log $output3 
		   -a=$threads
		   -v 
		   --paired_in 
		   --fastx
		   --aligned $output1
	   """
     }
}

unmerge_readpair = {
   doc "Unmerge read-pairs"  
   produce(input.prefix+"_1.fastq", input.prefix+"_2.fastq"){ 
      exec """
         $SORTMERNA/scripts/unmerge-paired-reads.sh $input.fastq $output1 $output2
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
            ILLUMINACLIP:$ROOT_DIR/adapter.fa:1:30:9 SLIDINGWINDOW:7:20 MINLEN:50
            
        """
    }
}

