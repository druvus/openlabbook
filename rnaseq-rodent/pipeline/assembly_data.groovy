////////////////////////////////////////////////////////////
// RNAseq-based pipeline
// Assembly step
 
CONFIG_DIR="/media/data/andreas/openlabbook/rnaseq-rodent/pipeline"

load "$CONFIG_DIR/config.groovy"  
load "$CONFIG_DIR/pipeline_stages_config.groovy"

//bpipe run assembly_data.groovy sample*.fq

run {   
    prepere_sortmerna_db + "%_*.fq" * [ merge_readpair + remove_rrna + unmerge_readpair ] + pool + trinity + bowtie_index + "%_*.fq" * [ bowtie_map ] + corset

}