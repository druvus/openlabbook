////////////////////////////////////////////////////////
//
// Pipeline stage definitions for RNAseq pipeline 
// See prepere_data.groovy for more information.
// 
////////////////////////////////////////////////////////

prepere_swissprot = {
      exec "wget --directory-prefix=blast http://downloads.sourceforge.net/project/trinotate/TRINOTATE_RESOURCES/20140708/uniprot_sprot.fasta.gz"
      exec "gzip -d $BLAST_DIR/uniprot_sprot.fasta.gz"
      exec "makeblastdb -in $BLAST_DIR/uniprot_sprot.fasta -dbtype prot"
}

prepere_pfamA = {
      exec "wget --directory-prefix=blast http://downloads.sourceforge.net/project/trinotate/TRINOTATE_RESOURCES/20140708/Pfam-A.hmm.gz"
      exec "gzip -d $BLAST_DIR/Pfam-A.hmm.gz"
      exec "hmmpress $BLAST_DIR/Pfam-A.hmm"
}

prepere_uniref90 = {
      exec "wget --directory-prefix=blast http://downloads.sourceforge.net/project/trinotate/TRINOTATE_RESOURCES/20140708/uniref90.fasta.gz"
      exec "gzip -d $BLAST_DIR/uniref90.fasta.gz"
      exec "makeblastdb -in $BLAST_DIR/uniref90.fasta -dbtype prot"
}

download_swissprot_db = {
      exec "wget --directory-prefix=trinotate_sqlite http://downloads.sourceforge.net/project/trinotate/TRINOTATE_RESOURCES/20140708/Trinotate.20140708.swissprot.sqlite.gz"
      exec "gzip -d $DB_DIR/Trinotate.20140708.swissprot.sqlite.gz"
}

download_swissTrEMBL_db = {
      exec "wget --directory-prefix=trinotate_sqlite http://downloads.sourceforge.net/project/trinotate/TRINOTATE_RESOURCES/20140708/Trinotate.20140708.swissTrEMBL.sqlite.gz"
      exec "gzip -d $DB_DIR/Trinotate.20140708.swissTrEMBL.sqlite.gz"
}

download_nt = {
   for (i in ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19'] ) {
      exec "echo $i"
      exec "wget --directory-prefix=blast ftp://ftp.ncbi.nlm.nih.gov/blast/db/nt.${i}.tar.gz"
   }
}

download_nr = {
   for (i in ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24'] ) {
      exec "wget --directory-prefix=blast ftp://ftp.ncbi.nlm.nih.gov/blast/db/nr.${i}.tar.gz"
   }
}

download_cow = {
   doc "Download Cow (B taurus)" 
   multi "wget -O $BLAST_DIR/cow_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/B_taurus/mRNA_Prot/cow.protein.faa.gz", 
   "wget -O $BLAST_DIR/cow_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/B_taurus/mRNA_Prot/cow.rna.fna.gz"
   multi "gzip -d $BLAST_DIR/cow_protein.fa.gz", 
   "gzip -d $BLAST_DIR/cow_rna.fa.gz"  
}

download_zebrafish = {
   doc "Download Zebrafish (D rerio)" 
   multi "wget -O $BLAST_DIR/zebrafish_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/D_rerio/mRNA_Prot/zebrafish.protein.faa.gz", 
   "wget -O $BLAST_DIR/zebrafish_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/D_rerio/mRNA_Prot/zebrafish.rna.fna.gz"
   multi "gzip -d $BLAST_DIR/zebrafish_protein.fa.gz", 
   "gzip -d $BLAST_DIR/zebrafish_rna.fa.gz"  
}

download_human = {
   doc "Download Human (H sapiens)" 
   multi "wget -O $BLAST_DIR/human_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/H_sapiens/mRNA_Prot/human.protein.faa.gz", 
   "wget -O $BLAST_DIR/human_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/H_sapiens/mRNA_Prot/human.rna.fna.gz"
   multi "gzip -d $BLAST_DIR/human_protein.fa.gz", 
   "gzip -d $BLAST_DIR/human_rna.fa.gz"  
}

download_mouse = {
   doc "Download Mouse (M musculus)" 
   multi "wget -O $BLAST_DIR/mouse_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/M_musculus/mRNA_Prot/mouse.protein.faa.gz", 
   "wget -O $BLAST_DIR/mouse_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/M_musculus/mRNA_Prot/mouse.rna.fna.gz"
   multi "gzip -d $BLAST_DIR/mouse_protein.fa.gz", 
   "gzip -d $BLAST_DIR/mouse_rna.fa.gz"  
}

download_rat = {
   doc "Download Rat (R norvegicus)" 
   multi "wget -O $BLAST_DIR/rat_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/R_norvegicus/mRNA_Prot/rat.protein.faa.gz", 
   "wget -O $BLAST_DIR/rat_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/R_norvegicus/mRNA_Prot/rat.rna.fna.gz"
   multi "gzip -d $BLAST_DIR/rat_protein.fa.gz", 
   "gzip -d $BLAST_DIR/rat_rna.fa.gz"  
}

download_pig = {
   doc "Download Pig (S scrofa)" 
   multi "wget -O $BLAST_DIR/pig_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/S_scrofa/mRNA_Prot/pig.protein.faa.gz", 
   "wget -O $BLAST_DIR/pig_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/S_scrofa/mRNA_Prot/pig.rna.fna.gz"
   multi "gzip -d $BLAST_DIR/pig_protein.fa.gz", 
   "gzip -d $BLAST_DIR/pig_rna.fa.gz"  
}


download_frog = {
   doc "Download Frog (X tropicalis) RNA" 
   multi "wget -O $BLAST_DIR/frog_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/X_tropicalis/mRNA_Prot/frog.protein.faa.gz", 
   "wget -O $BLAST_DIR/frog_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/refseq/X_tropicalis/mRNA_Prot/frog.rna.fna.gz"
   multi "gzip -d $BLAST_DIR/frog_protein.fa.gz", 
   "gzip -d $BLAST_DIR/frog_rna.fa.gz"  
}

prepere_ncbiref_protein = {
exec "cat $BLAST_DIR/cow_protein.fa $BLAST_DIR/zebrafish_protein.fa $BLAST_DIR/human_protein.fa $BLAST_DIR/mouse_protein.fa $BLAST_DIR/rat_protein.fa $BLAST_DIR/pig_protein.fa $BLAST_DIR/frog_protein.fa > $BLAST_DIR/ncbi_ref_protein.faa"
exec "makeblastdb -in $BLAST_DIR/ncbi_ref_protein.faa -dbtype prot"
}

prepere_ncbiref_rna = {
exec "cat $BLAST_DIR/cow_rna.fa $BLAST_DIR/zebrafish_rna.fa $BLAST_DIR/human_rna.fa $BLAST_DIR/mouse_rna.fa $BLAST_DIR/rat_rna.fa $BLAST_DIR/pig_rna.fa $BLAST_DIR/frog_rna.fa > $BLAST_DIR/ncbi_ref_rna.fna"
exec "makeblastdb -in $BLAST_DIR/ncbi_ref_rna.fna -dbtype nucl"
}


download_chinesehamster = {
   doc "Download Chinese hamster (Cricetulus griseus)" 
   multi "wget -O $BLAST_DIR/chinesehamster_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/genomes/Cricetulus_griseus/protein/protein.fa.gz",
         "wget -O $BLAST_DIR/chinesehamster_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/genomes/Cricetulus_griseus/RNA/rna.fa.gz"
   multi "gzip -d $BLAST_DIR/chinesehamster_protein.fa.gz", 
         "gzip -d $BLAST_DIR/chinesehamster_rna.fa.gz"
}

download_goldenhamster = {
   doc "Download Golden hamster (Mesocricetus auratus)" 
   multi "wget -O $BLAST_DIR/goldenhamster_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/genomes/Mesocricetus_auratus/protein/protein.fa.gz", 
         "wget -O $BLAST_DIR/goldenhamster_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/genomes/Mesocricetus_auratus/RNA/rna.fa.gz"
   multi "gzip -d $BLAST_DIR/goldenhamster_protein.fa.gz",
         "gzip -d $BLAST_DIR/goldenhamster_rna.fa.gz"
}

download_prairiedeermouse =  {
   doc "Download Prairie Deer mouse (Peromyscus maniculatus bairdii)" 
   multi "wget -O $BLAST_DIR/prairiedeermouse_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/genomes/Peromyscus_maniculatus_bairdii/protein/protein.fa.gz",
         "wget -O $BLAST_DIR/prairiedeermouse_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/genomes/Peromyscus_maniculatus_bairdii/RNA/rna.fa.gz"
   multi "gzip -d $BLAST_DIR/prairiedeermouse_protein.fa.gz",
         "gzip -d $BLAST_DIR/prairiedeermouse_rna.fa.gz"
}

download_prairievole = {
   doc "Download Prarie vole (Microtus ochrogaster)" 
   multi "wget -O $BLAST_DIR/prairievole_protein.fa.gz ftp://ftp.ncbi.nlm.nih.gov/genomes/Microtus_ochrogaster/protein/protein.fa.gz", 
         "wget -O $BLAST_DIR/prairievole_rna.fa.gz ftp://ftp.ncbi.nlm.nih.gov/genomes/Microtus_ochrogaster/RNA/rna.fa.gz"
   multi "gzip -d $BLAST_DIR/prairievole_protein.fa.gz",
         "gzip -d $BLAST_DIR/prairievole_rna.fa.gz"
}

prepere_extended_protein = {
exec "cat $BLAST_DIR/ncbi_ref_protein.faa $BLAST_DIR/chinesehamster_protein.fa $BLAST_DIR/goldenhamster_protein.fa $BLAST_DIR/prairiedeermouse_protein.fa $BLAST_DIR/prairievole_protein.fa  > $BLAST_DIR/ncbi_extended_protein.faa"
exec "makeblastdb -in $BLAST_DIR/ncbi_extended_protein.faa -dbtype prot"
}

prepere_extended_rna = {
exec "cat $BLAST_DIR/ncbi_ref_rna.fna $BLAST_DIR/chinesehamster_rna.fa $BLAST_DIR/goldenhamster_rna.fa $BLAST_DIR/prairiedeermouse_rna.fa $BLAST_DIR/prairievole_rna.fa  > $BLAST_DIR/ncbi_extended_rna.fna "
exec "makeblastdb -in $BLAST_DIR/ncbi_extended_rna.fna -dbtype nucl"
}


download_bankvole_pooled = {
   doc "Download pooled transcriptome of the bank vole" 
   multi "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010537/SRR1010537.sra", 
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010629/SRR1010629.sra",
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010630/SRR1010630.sra",
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010631/SRR1010631.sra",
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010821/SRR1010821.sra",
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010832/SRR1010832.sra", 
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010833/SRR1010833.sra", 
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010834/SRR1010834.sra", 
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010835/SRR1010835.sra", 
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010836/SRR1010836.sra", 
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP030/SRP030778/SRR1010837/SRR1010837.sra"
}


download_bankvole_heart = {
   doc "Download Heart transcriptome of the bank vole" 
   multi "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP002/SRP002377/SRR042424/SRR042424.sra", 
   "wget -P $SRA_DIR ftp://ftp.ncbi.nlm.nih.gov/sra/sra-instant/reads/ByStudy/sra/SRP/SRP002/SRP002377/SRR042425/SRR042425.sra"
}

