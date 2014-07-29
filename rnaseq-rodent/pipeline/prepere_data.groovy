////////////////////////////////////////////////////////////
// RNAseq-based pipeline
// 
CONFIG_DIR="/media/data/andreas/openlabbook/rnaseq-rodent/pipeline"

load "$CONFIG_DIR/config.groovy"  
load "$CONFIG_DIR/pipeline_stages_config.groovy"

run {   prepere_swissprot + prepere_pfamA  + 
        prepere_uniref90 + download_swissprot_db + download_swissTrEMBL_db  + 
        download_nt + download_nr  +  
        download_cow + download_zebrafish + download_human + download_mouse + download_rat + download_pig + download_frog  +
        download_chinesehamster + download_goldenhamster + download_prairiedeermouse + download_prairievole  +
      [ prepere_ncbiref_protein + prepere_ncbiref_rna + prepere_extended_protein + prepere_extended_rna ] 

}
