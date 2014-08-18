////////////////////////////////////////////////////////
//
// Pipeline stage definitions for RNAseq pipeline 
// See prepere_data.groovy for more information.
// 
////////////////////////////////////////////////////////

prepere_sortmerna_rfam_5s = {
   doc "Creating binary database rfam-5s"  
   exec "$SORTMERNA/indexdb_rna --ref $SORTMERNA/rRNA_databases/rfam-5s-database-id98.fasta,$SORTMERNA/index/rfam-5s"
}

prepere_sortmerna_rfam_58s = {
   doc "Creating binary database rfam-58s"  
   exec "$SORTMERNA/indexdb_rna --ref $SORTMERNA/rRNA_databases/rfam-5.8s-database-id98.fasta,$SORTMERNA/index/rfam-5.8s"
}

prepere_sortmerna_bac_16s = {
   doc "Creating binary database bac-16s"  
   exec "$SORTMERNA/indexdb_rna --ref $SORTMERNA/rRNA_databases/silva-bac-16s-database-id85.fasta,$SORTMERNA/index/silva-bac-16s"
}

prepere_sortmerna_bac_23s = {
   doc "Creating binary database bac-23s"
   exec "$SORTMERNA/indexdb_rna --ref $SORTMERNA/rRNA_databases/silva-bac-23s-database-id98.fasta,$SORTMERNA/index/silva-bac-23s"
}


prepere_sortmerna_euk_18s = {
   doc "Creating binary database euk-18s"  
   exec "$SORTMERNA/indexdb_rna --ref $SORTMERNA/rRNA_databases/silva-euk-18s-database-id95.fasta,$SORTMERNA/index/silva-euk-18s"
}

prepere_sortmerna_euk_28s = {
   doc "Creating binary database euk-28s"  
   exec "$SORTMERNA/indexdb_rna --ref $SORTMERNA/rRNA_databases/silva-euk-28s-database-id98.fasta,$SORTMERNA/index/silva-euk-28s"
}

prepere_sortmerna_arc_16s = {
   doc "Creating binary database euk-16s"
   exec "$SORTMERNA/indexdb_rna --ref $SORTMERNA/rRNA_databases/silva-arc-16s-database-id95.fasta,$SORTMERNA/index/silva-arc-16s"
}

prepere_sortmerna_arc_23s = {
   doc "Creating binary database arc-23s"
   exec "$SORTMERNA/indexdb_rna --ref $SORTMERNA/rRNA_databases/silva-arc-23s-database-id98.fasta,$SORTMERNA/index/silva-arc-23s"
}


prepere_sortmerna_db = segment {
 [ prepere_sortmerna_rfam_5s + prepere_sortmerna_rfam_58s + prepere_sortmerna_bac_16s + prepere_sortmerna_bac_23s +  prepere_sortmerna_bac_16s + prepere_sortmerna_arc_23s + prepere_sortmerna_euk_18s + prepere_sortmerna_euk_28s ]
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
    def output =  file(input.fq).name.replaceAll('_([1-2]).fq$','' + '.merge_readpair.fastq')
    
  
   exec """
   $SORTMERNA/scripts/merge-paired-reads.sh $input1.fq $input2.fq $output
   """
}


remove_rrna = {
   doc "Remove rRNA from reads"
     produce(input.prefix+"_accepted.fastq", input.prefix+"_rejected.fastq", input.prefix+"_sortmerna.log"){ 
	   exec """
		   $SORTMERNA/sortmerna 
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
         $SORTMERNA/scripts/unmerge-paired-reads.sh $input $output1 $output2
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

