#!/usr/bin/env python
# -*- coding: utf-8 -*-
 
#title           :extract_qiime_otuseq.py
#description     :This script will extract sequence reads belonging to OTU with matching assigned taxonomy.
#author          :Andreas Sjödin
#date            :20140611
#version         :0.1

#==============================================================================


########################################################
##
## Import required modules
##
########################################################

from Bio import SeqIO
from Bio.SeqUtils.CheckSum import seguid
from collections import defaultdict
import csv
import re
import sys
import argparse

########################################################
##
## Parse the command line options
##
########################################################

parser = argparse.ArgumentParser()  # Initiate argument parser object
parser.add_argument('-t', '--otutable', help='File containing read counts and taxonomy for each OTU', required=True)
parser.add_argument('-s', '--otuseq', help='File containing read names for each OTU', required=True)
parser.add_argument('-q', '--query', help='Search term to use in filtering', required=True)
parser.add_argument("-f", "--fastafile", help="Sequence read file output from Qiime")
args = parser.parse_args()  # Call command line parser method

########################################################
##
## Define file readers and writers
##
########################################################

otu_read = args.otutable
otu_seq = args.otuseq
search_term = args.query
fasta_read = args.fastafile

otu_seq_write = search_term + '_otus_map.txt'
seq_count_write = search_term + '_seq_count.txt'

otu_write = 'out_table_' + search_term + '.txt'

wotufile  = open(otu_write, "wb")
otuwriter = csv.writer(wotufile, delimiter='\t')

wotuseqfile  = open(otu_seq_write, "wb")
otuseqwriter = csv.writer(wotuseqfile, delimiter='\t')

seqcountfile  = open(seq_count_write, "wb")
seqcountwriter = csv.writer(seqcountfile, delimiter='\t')


otu_dict = {}
otu_count = 0

########################################################
##
## Open OTU table file and extract matching OTUs
##
########################################################
print ""
print "########################################################"
print "#"
print "# Starting analysis"
print "#"
print "########################################################"
print ""

print 'Open OTU file: ' + otu_read

with open(otu_read, 'rb') as f:
    reader = csv.reader(f, delimiter='\t')
    for row in reader:
        if re.search('#OTU', row[0]):
            otuwriter.writerow(row)
        else:
            if re.search(search_term, row[-1]):
                otuwriter.writerow(row)
                otu_dict[row[0]] = row[-1]
                otu_count += 1
                
        

print 'Write matching OTU to file: ' + otu_write
print 'Found ' + str(otu_count) + ' OTUs matching search term "' + search_term + '"'

########################################################
##
## Open OTU-read file and extract reads in interesting OTU
##
########################################################

sequences = []

print ""
print "########################################################"
print "#"
print "# Summary of each OTU of interest"
print "#"
print "########################################################"
print ""


with open(otu_seq, 'rb') as fseq:
    reader = csv.reader(fseq, delimiter='\t')
    for row in reader:
        if row[0] in otu_dict:
            print 'OTU: ' +  row[0]
            print 'Taxonomy: ' + otu_dict[row[0]]
            print 'Number of ' + search_term + ' reads: ' + str(len(row))
            print''
            otuseqwriter.writerow(row)
            sequences = sequences + row[1::]

otu_seqid = set(sequences)

########################################################
##
## Open fasta file (if argument exist) and extract reads of interest
##
########################################################

if args.fastafile:
    print ""
    print "########################################################"
    print "#"
    print "# Extraction of sequence reads from fasta file"
    print "#"
    print "########################################################"
    print ""
    print 'Loading fastafile: ' + fasta_read
    checksums = defaultdict(defaultdict)
    
    
    handle = open(fasta_read, "rU")
    for record in SeqIO.parse(handle, "fasta") :
        if record.id in otu_seqid:
            checksum = seguid(record.seq)
            if checksum in checksums:
                checksums[checksum]['count'] += 1 
                continue
            checksums[checksum]['id'] = record.id
            checksums[checksum]['seq'] = record.seq
            checksums[checksum]['count'] = 1
            checksums[checksum]['length'] = len(record.seq)
        
    handle.close()
    
    print 'Writing sequnce count file: ' + seq_count_write
    
    seqcountwriter.writerow(['ID' , 'Sequence', '#sequences', 'length', 'Representative read' ])
    
    for checksum in checksums:
        seqcountwriter.writerow(["'"+checksum, checksums[checksum]['seq'], checksums[checksum]['count'], checksums[checksum]['length'], checksums[checksum]['id']  ])

print ""
print "########################################################"
print "#"
print "# Analysis done"
print "#"
print "########################################################"
print ""
