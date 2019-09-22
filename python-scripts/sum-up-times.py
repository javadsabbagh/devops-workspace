import os
import subprocess
out = subprocess.Popen(['ls'],
                       stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
stdout,stderr = out.communicate()
print(stdout)
