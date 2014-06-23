#!/usr/bin/env python
# -*- coding: utf-8 -*-

__copyright__ = """
Copyright (C) 2014 - Andreas sjödin

"""

__desc__ = """"""
__created__ = "June 21, 2014"
__author__ = "Andreas Sjödin"

import argparse
import csv
import glob
import sys, re, os
from collections import defaultdict


parser = argparse.ArgumentParser()  # Initiate argument parser object
parser.add_argument('-f', '--folder', help='Folder containing GATK coverage summary files', required=True)
parser.add_argument('-o', '--out', help='File name of output file', required=True)
parser.add_argument('-c', '--cutoff', help='Search term to use in filtering', default=30, type=int)
args = parser.parse_args()  # Call command line parser method


workingdirectory = args.folder
outputfile = args.out
cutoff = args.cutoff


covdict = defaultdict(defaultdict)

os.chdir(workingdirectory)

print ""
print "########################################################"
print "#"
print "# Starting analysis"
print "#"
print "########################################################"
print ""

print ''
print 'Analysing coverage summary files in directory: ' + workingdirectory
print ''
  
print 'Found the following coverage summary files'
print '-------------------------'

coveragefiles = glob.glob("*_interval_summary")
for coveragefile in coveragefiles:
    print coveragefile

print ''    
print 'Samples with low coverage'
print '-------------------------'

 
for coveragefile in coveragefiles:
    sample = str(coveragefile).split('.')[0]
    with open(coveragefile, 'rb') as f:
        reader = csv.reader(f, delimiter='\t')
        for row in reader:
            if re.search('Target', row[0]):
                header = row 
            else:
                chrom = row[0].split(':')[0]
                coverage = row[2]
                if float(coverage) <= cutoff:
                    print 'Sample ' + sample + ' and chromosome ' + chrom + ' is below cutoff (' + str(cutoff) + 'x): ' + coverage + 'x'
                covdict[sample][chrom] = coverage
                
                

#chromosomes = covdict[sample].keys()
chromosomes = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', 'Y', 'X', 'MT']
samples = covdict.keys()


summaryfile  = open(outputfile, "wb")
summarywriter = csv.writer(summaryfile, delimiter='\t')
summarywriter.writerow(['Sample'] + chromosomes)

for sample in samples:
    #print 'Writing results of sample: ' + sample
    outdata = [sample]
    for chromosome in chromosomes:
        outdata.append(covdict[sample][chromosome])
    summarywriter.writerow(outdata)
    
print ''
print 'Wrote table to file: ' + outputfile

print ""
print "########################################################"
print "#"
print "# Analysis finished"
print "#"
print "########################################################"
print ""