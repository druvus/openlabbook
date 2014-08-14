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
SORTMERNA="/home/andreas/src/sortmerna"
db5s=$SORTMERNADIR/rRNA_databases/rfam-5s-database-id98.fasta
db58s=$SORTMERNADIR/rRNA_databases/rfam-5.8s-database-id98.fasta
db16s=$SORTMERNADIR/rRNA_databases/silva-bac-16s-database-id85.fasta
db18s=$SORTMERNADIR/rRNA_databases/silva-euk-18s-database-id95.fasta
db23s=$SORTMERNADIR/rRNA_databases/silva-bac-23s-database-id98.fasta
db28s=$SORTMERNADIR/rRNA_databases/silva-euk-28s-database-id98.fasta
dbNum=6
dbs="$db5s $db58s $db16s $db18s $db23s $db28s"


//Set location of trimmomatic
TRIMMOMATIC="/home/andreas/src/Trimmomatic-0.32"