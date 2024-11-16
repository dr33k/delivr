CREATE TABLE product_product_tag(
product_id INTEGER NOT NULL,
product_tag_id INTEGER NOT NULL,
PRIMARY KEY(product_id,product_tag_id),

CONSTRAINT ppt_pid_fk
FOREIGN KEY(product_id)
REFERENCES product(id)
ON DELETE CASCADE,

CONSTRAINT ppt_ptid_fk
FOREIGN KEY(product_tag_id)
REFERENCES product_tag(id)
ON DELETE CASCADE
);