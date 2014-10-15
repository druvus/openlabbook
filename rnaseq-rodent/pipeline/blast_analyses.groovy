////////////////////////////////////////////////////////////
// RNAseq-based pipeline
// Assembly step
 
CONFIG_DIR="/media/data/andreas/openlabbook/rnaseq-rodent/pipeline"

load "$CONFIG_DIR/config.groovy"  
load "$CONFIG_DIR/pipeline_stages_config.groovy"
load "$CONFIG_DIR/rnaseq_pipeline_stages_config.groovy"

//bpipe run assembly_data.groovy sample*.fq

run {   
   blastn_ncbi_ref_rna + blastn_ncbi_extended_rna  + blastn_nt +
   blastx_uniref90 + blastx_ncbi_ref_protein + blastx_ncbi_extended_protein + blastx_nr + blastx_uniprot_sprot
}
