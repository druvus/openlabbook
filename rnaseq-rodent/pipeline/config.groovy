//Set location of analysis folder
ROOT_DIR="/media/data/andreas/vole"

//Set location of blast data folder
BLAST_DIR="${ROOT_DIR}/blast"

//Set location of Trinotate database folder
DB_DIR="${ROOT_DIR}/trinotate_sqlite"

//Set location of SRA raw reads folder
SRA_DIR="${ROOT_DIR}/SRA"

//Set location of analysis software
TRINITY="/home/andreas/bin/trinityrnaseq_r20140717"
CORSET="/usr/local/corset/corset-0.9"

//Set location of sortmerna and databases
SORTMERNA="/home/andreas/src/sortmerna-1.9"
SORTMERNADIR="/home/andreas/src/sortmerna-1.9"

// sortmerna 1.99
//db5s_rfam="$SORTMERNA/rRNA_databases/rfam-5s-database-id98.fasta,$SORTMERNA/index/rfam-5s"
//db58s_rfam="$SORTMERNA/rRNA_databases/rfam-5.8s-database-id98.fasta,$SORTMERNA/index/rfam-5.8s"
//db16s_bac="$SORTMERNA/rRNA_databases/silva-bac-16s-database-id85.fasta,$SORTMERNA/index/silva-bac-16s"
//db23s_bac="$SORTMERNA/rRNA_databases/silva-bac-23s-database-id98.fasta,$SORTMERNA/index/silva-bac-23s"
//db18s_euk="$SORTMERNA/rRNA_databases/silva-euk-18s-database-id95.fasta,$SORTMERNA/index/silva-euk-18s"
//db28s_euk="$SORTMERNA/rRNA_databases/silva-euk-28s-database-id98.fasta,$SORTMERNA/index/silva-euk-28s"
//db16s_arc="$SORTMERNA/rRNA_databases/silva-arc-16s-database-id95.fasta,$SORTMERNA/index/silva-arc-16s"
//db23s_arc="$SORTMERNA/rRNA_databases/silva-arc-23s-database-id98.fasta,$SORTMERNA/index/silva-arc-23s"
//dbs="$db5s_rfam:$db58s_rfam:$db16s_bac:$db23s_bac:$db18s_euk:$db28s_euk:$db16s_arc:$db23s_arc"

//sortmerna 1.9
db5s_rfam="$SORTMERNA/rRNA_databases/rfam-5s-database-id98.fasta"
db58s_rfam="$SORTMERNA/rRNA_databases/rfam-5.8s-database-id98.fasta"
db16s_bac="$SORTMERNA/rRNA_databases/silva-bac-16s-database-id85.fasta"
db23s_bac="$SORTMERNA/rRNA_databases/silva-bac-23s-database-id98.fasta"
db18s_euk="$SORTMERNA/rRNA_databases/silva-euk-18s-database-id95.fasta"
db28s_euk="$SORTMERNA/rRNA_databases/silva-euk-28s-database-id98.fasta"
db16s_arc="$SORTMERNA/rRNA_databases/silva-arc-16s-database-id95.fasta"
db23s_arc="$SORTMERNA/rRNA_databases/silva-arc-23s-database-id98.fasta"
dbNum="8"
dbs="$db5s_rfam $db58s_rfam $db16s_bac $db23s_bac $db18s_euk $db28s_euk $db16s_arc $db23s_arc"

//Set location of trimmomatic
TRIMMOMATIC="/home/andreas/src/Trimmomatic-0.32"

//Set location of Trinotate software
TRINOTATE="/home/andreas/src/Trinotate_r20140708"
TMHMM="/home/andreas/src/tmhmm/tmhmm-2.0c/bin"
RNAMMER="/home/andreas/src/rnammer"
SIGNALP="/home/andreas/src/signalp/signalp-4.1"

