# Author = Sivkov Alexander Vladimirovich
# Group = P3116
# Date = 06.11.2025
# Variant 11

import sys
import os
import yaml
import hcl2

sys.path.append(os.path.dirname(__file__))

def parse(string):
    loaded_data = yaml.safe_load(string)
    new_ast = hcl2.reverse_transform(loaded_data)
    new_hcl = hcl2.writes(new_ast)
    return new_hcl


def run_task_2(input_filename, output_filename):
    string = open(input_filename, "r", encoding='UTF-8').read()
    open(output_filename, "w", encoding='UTF-8').write(parse(string))


if __name__ == '__main__':
    input_file = os.path.join(os.path.dirname(__file__), "../docs/in.yaml")
    output_file = os.path.join(os.path.dirname(__file__), "../docs/out2.hcl")
    run_task_2(input_file, output_file)
    print('Complete!')
