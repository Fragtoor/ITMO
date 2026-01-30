
import yaml
from bs4 import BeautifulSoup, NavigableString

class IndentedListDumper(yaml.SafeDumper):
    def increase_indent(self, flow=False, indentless=False):
        return super(IndentedListDumper, self).increase_indent(flow, False)

def html_to_dict(tag):
    node = {'tag': tag.name}
    
    if tag.attrs:
        node['attributes'] = {
            key: value if not isinstance(value, list) else list(value)
            for key, value in tag.attrs.items()
        }

    children = []
    for child in tag.children:
        if isinstance(child, NavigableString):
            text = child.strip()
            if text:
                children.append({'text': text})
        else:
            children.append(html_to_dict(child))
    
    if children:
        node['children'] = children

    return node

def convert_html_to_yaml(html_string):
    soup = BeautifulSoup(html_string, 'html.parser')

    root_element = next((tag for tag in soup.children if tag.name is not None), None)
    
    if not root_element:
        return ""

    data_structure = html_to_dict(root_element)

    with open('in2.yaml', 'w', encoding='UTF-8') as f:
        
        yaml.dump(
            data_structure,
            Dumper=IndentedListDumper,
            allow_unicode=True,
            sort_keys=False,
            indent=2
        )

html_code = open('in.html', encoding='UTF-8').read()

convert_html_to_yaml(html_code)
