////////////////////////////////////////////////////////////
// RNAseq-based pipeline
// Assembly step
 
CONFIG_DIR="/media/data/andreas/openlabbook/rnaseq-rodent/pipeline"

load "$CONFIG_DIR/config.groovy"  
load "$CONFIG_DIR/pipeline_stages_config.groovy"
load "$CONFIG_DIR/rnaseq_pipeline_stages_config.groovy"

//bpipe run assembly_data.groovy sample*.fq

run {   
  "%_*.fq" * [ trinity_assembly + trinity_stats ]
}
