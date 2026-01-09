#This code transforms a .csv file into a .gz file

import pandas as pd

input_file = "emails.csv"

output_file = "emails.csv.gz"

df = pd.read_csv(input_file)

df.to_csv(output_file, index=False, compression="gzip")

print(f"Compressed file saved as: {output_file}")
