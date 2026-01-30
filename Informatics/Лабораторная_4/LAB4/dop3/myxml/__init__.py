
def dump(obj):
    main_list = ['<?xml version="1.0" encoding="UTF-8"?>']
    to_string(obj, main_list, 0)
    return '\n'.join(main_list)
    

def to_string(obj, main_list, indent):     
    if not obj:
        return
    
    keys = []
    if isinstance(obj, dict):
        keys = list(obj.keys())
        is_list = False
    else:
        is_list = True
    
    if not is_list:
        for i, key in enumerate(keys):
            if (not isinstance(obj[key], list)) and (not isinstance(obj[key], dict)):
                if isinstance(obj, list):
                    main_list += ['\t' * indent + '<item>' + str(obj[key]) + '</item>']
                else:
                    main_list += ['\t' * indent + f'<{key}>' + str(obj[key]) + f'</{key}>']
                continue

            if isinstance(obj[key], list) or isinstance(obj[key], dict):
                main_list += ['\t' * indent + '<' + key + '>']
                to_string(obj[key], main_list, indent + 1)
                main_list += ['\t' * indent + '</' + key + '>']
            else:
                main_list += ['\t' * indent + '<' + key + '>' + str(obj[key]) + '<' + key + '/>']

    else:
        for i, elem in enumerate(obj):
            if isinstance(elem, list) or isinstance(elem, dict):
                main_list += ['\t' * indent + '<item>']
                to_string(elem, main_list, indent + 1)
                main_list += ['\t' * indent + '</item>']
            else:
                main_list += ['\t' * indent + '<item>' + str(elem) + '</item>']
