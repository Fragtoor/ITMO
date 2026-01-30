# Author = Sivkov Alexander Vladimirovich
# Group = P3116
# Date = 06.11.2025
# Variant 11

import sys
import os

sys.path.append(os.path.dirname(__file__))

import mychl as chl
import myyaml as yaml


def parse(string):
    return chl.dump(yaml.loads(string))


def run_task_1(input_filename, output_filename):
    string = open(input_filename, "r", encoding='UTF-8').read()
    open(output_filename, "w", encoding='UTF-8').write(parse(string))


if __name__ == "__main__":
    input_file = os.path.join(os.path.dirname(__file__), "../docs/in.yaml")
    output_file = os.path.join(os.path.dirname(__file__), "../docs/out.hcl")
    
    run_task_1(input_file, output_file)
    print("Complete!")
