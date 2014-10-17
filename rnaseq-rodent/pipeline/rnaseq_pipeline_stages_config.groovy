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
    def output_name = "$input".split("/")[-1] // remove path to file
    output_name = output_name.prefix.prefix.split("_")[0] // only use common name for output
    
    produce("${output_name}.p1.fastq","${output_name}.u1.fastq","${output_name}.p2.fastq","${output_name}.u2.fastq") {
        exec """
            java -jar $TRIMMOMATIC/trimmomatic-0.32.jar PE 
            -threads $threads 
            -phred33 
            $input1.fastq $input2.fastq $output1 $output2 $output3 $output4 
            ILLUMINACLIP:$ROOT_DIR/adapter.fa:1:30:9 SLIDINGWINDOW:7:20 MINLEN:50
            
        """
    }
}

normalize_reads = {
   doc "Normalize read-pairs"
   produce(input.prefix+"_norm_1.fastq", input.prefix+"_norm_2.fastq"){
      exec """
         $TRINITY/util/insilico_read_normalization.pl --seqType fq --JM 20G --max_cov 50 --left $input1 --right $input2 --pairs_together --PARALLEL_STATS --CPU 5
      """
   }
}

pool = {
    produce("all_1.fastq","all_2.fastq") {
        from("*.p1.fastq") {
            exec """
                cat $inputs.fastq > $output1
            """
        }

        from("*.p2.fastq") {
            exec """
                cat $inputs.fastq > $output2
            """
        }
    }
}

count_reads = {
   doc "Count number of reads in fastq file"
   exec "grep '@HWI-' $input | wc -l > $output"
}


trinity_assembly = {
   doc "Count number of reads in fastq file"
   output.dir="k1_trinity"
   from ("_1.fastq*", "_2.fastq*") produce ("Trinity.fasta") {
      exec "$TRINITY/Trinity --seqType fq --JM 40G --left $input1 --right $input2 --CPU $threads --min_kmer_cov 1 --output $output.dir"
   }
}


trinity_stats = {
   doc "Generate statistics of the assembly"
      exec "$TRINITY/util/TrinityStats.pl $input"
}

align_estimate_abundance = {
   doc "Prepere reference and align reads"
   produce ("RSEM.genes.results", "RSEM.isoforms.results"){
      exec "$TRINITY/util/align_and_estimate_abundance.pl --transcripts $input1 --seqType fq --left $input2 --right $input3 --est_method RSEM --aln_method bowtie --trinity_mode --prep_reference"
   }
}

count_features = {
   doc "Countnumber of genes"
   produce ("cumul_counts.txt"){
      exec "$TRINITY/util/misc/count_features_given_MIN_FPKM_threshold.pl $input  > $output"
   }
}

filter_fasta_rsem = {
   doc "Filter away genes without read support"
   produce ("Trinity_filtered.fasta") {
   from ("RSEM.isoforms.results", "Trinity.fasta"){
      exec "$TRINITY/util/filter_fasta_by_rsem_values.pl  --rsem_output=$input1 --fasta=$input2 --output=$output --fpkm_cutoff=2 "
   }
   }
}

transdecoder_pfam = {
   doc "Extract Likely Coding Sequences from Trinity Transcripts (Pfam-version)"
   from ("Trinity_filtered.fasta") {
      produce ("Trinity_filtered.fasta.transdecoder.pep"){
      exec "$TRINITY/trinity-plugins/transdecoder/TransDecoder -t $input -m 50 --CPU $threads --search_pfam $BLAST_DIR/Pfam-AB.hmm.bin --workdir transdecoder --reuse"
   }
   }
}

transdecoder = {
   doc "Extract Likely Coding Sequences from Trinity Transcripts"
   from ("Trinity_filtered.fasta") {
      produce ("Trinity_filtered.fasta.transdecoder.pep"){
      exec "$TRINITY/trinity-plugins/transdecoder/TransDecoder -t $input -m 50 --CPU $threads --workdir transdecoder --reuse"
   }
   }
}

blastx_uniprot_sprot = {
   doc "Blast genes against sprot"
   from ("Trinity_filtered.fasta") {
      exec "blastx -query $input -db $BLAST_DIR/uniprot_sprot.fasta -out $output -evalue 1e-20 -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}

blastx_nr = {
   doc "Blast genes against nr"
   from ("Trinity_filtered.fasta") {
      exec "blastx -query $input -db $BLAST_DIR/nr -out $output -evalue 1e-20 -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}

blastx_ncbi_extended_protein = {
   doc "Blast genes against extended set of NCBI reference genomes"
   from ("Trinity_filtered.fasta") {
      exec "blastx -query $input -db $BLAST_DIR/ncbi_extended_protein.faa -out $output -evalue 1e-20 -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}


blastx_ncbi_ref_protein = {
   doc "Blast genes against NCBI reference genomes"
   from ("Trinity_filtered.fasta") {
      exec "blastx -query $input -db $BLAST_DIR/ncbi_ref_protein.faa -out $output -evalue 1e-20 -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}

blastx_uniref90 = {
   doc "Blast genes against uniref90"
   from ("Trinity_filtered.fasta") {
      exec "blastx -query $input -db $BLAST_DIR/uniref90.fasta -out $output -evalue 1e-20 -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}

blastn_nt = {
   doc "Blast genes against nt"
   from ("Trinity_filtered.fasta") {
      exec "blastn -query $input -db $BLAST_DIR/nt -out $output -evalue 1e-20 -dust no -task megablast -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}

blastn_ncbi_extended_rna = {
   doc "Blast genes against extended set of NCBI reference genomes"
   from ("Trinity_filtered.fasta") {
      exec "blastn -query $input -db $BLAST_DIR/ncbi_extended_rna.fna -out $output -evalue 1e-20 -dust no -task megablast -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}

blastn_ncbi_ref_rna = {
   doc "Blast genes against nt"
   from ("Trinity_filtered.fasta") {
      exec "blastn -query $input -db $BLAST_DIR/ncbi_ref_rna.fna -out $output -evalue 1e-20 -dust no -task megablast -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}

blastp_uniref90 = {
   doc "Blast genes against uniref90"
   from ("transdecoder.pep") {
      exec "blastp -query $input -db $BLAST_DIR/uniref90.fasta -out $output -evalue 1e-20 -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}

blastp_uniprot_sprot = {
   doc "Blast genes against sprot"
   from ("transdecoder.pep") {
      exec "blastp -query $input -db $BLAST_DIR/uniprot_sprot.fasta -out $output -evalue 1e-20 -num_threads $threads -max_target_seqs 1 -outfmt 6"
   }
}


hmmer = {
   doc "Running HMMER to identify protein domains"
   from ("transdecoder.pep") {
      exec "hmmscan --cpu $threads --domtblout $output $BLAST_DIR/Pfam-A.hmm $input > pfam.log"
   }
}

signalp = {
   doc "Running signalP to predict signal peptides"
   from ("transdecoder.pep") {
      exec "$SIGNALP/signalp -f short -n $output $input"
   }
}

tmhmm = {
   doc "Running tmHMM to predict transmembrane regions"
   from ("transdecoder.pep") {
      exec "$TMHMM/tmhmm --short < $input > $output"
   }
}

rnammer = {
   doc "Running RNAMMER to identify rRNA transcripts"
   from ("Trinity_filtered.fasta") {
      exec "$TRINOTATE/util/rnammer_support/RnammerTranscriptome.pl --transcriptome $input --path_to_rnammer $RNAMMER/rnammer"
   }
}
