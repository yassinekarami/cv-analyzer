import streamlit as st
import pandas as pd
import numpy as np
import requests

from io import BytesIO

st.title('Upload CV')

file_upload_form = st.form("file_upload")

file_to_upload = file_upload_form.file_uploader(
    "Upload file as pdf",
    type="pdf"
)
submit = file_upload_form.form_submit_button("Upload file")

if submit:
    if file_to_upload is None:
        st.error("Please upload a file first")
    else:
        bytes_data = file_to_upload.getvalue()

        files = {
            "file": ("uploaded.pdf", BytesIO(bytes_data), "application/pdf")
        }

        response = requests.post("http://localhost:8080/upload", files=files)

        if response.status_code == 200:
            st.success("Uploaded file")
        else:
            st.error("Upload failed")
