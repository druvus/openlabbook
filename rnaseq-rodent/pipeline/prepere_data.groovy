////////////////////////////////////////////////////////////
// RNAseq-based pipeline
// 
CONFIG_DIR="/media/data/andreas/openlabbook/rnaseq-rodent/pipeline"

load "$CONFIG_DIR/config.groovy"  
load "$CONFIG_DIR/pipeline_stages_config.groovy"

//run { [ prepere_swissprot + prepere_pfamA + prepere_uniref90 + download_swissprot_db + download_swissprot_db  ] }
run { [ prepere_swissprot + prepere_pfamA  ] + 
      [ prepere_uniref90 + download_swissprot_db + download_swissTrEMBL_db ] + 
      [ download_nt + download_nr + download_ncbiref + download_close ] +
      [ prepere_ncbiref_protein + prepere_ncbiref_rna + prepere_extended_protein + prepere_extended_rna ] 



}
