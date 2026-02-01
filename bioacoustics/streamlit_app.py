import io
import os
import tempfile

import bioacoustics_model_zoo as bmz
import pandas as pd
import streamlit as st


def main():
    st.set_page_config(
        page_title="Bioacoustic classification",
        page_icon="🐦",
    )

    st.header("Bioacoustic classification")

    files = st.file_uploader(
        "Upload",
        type="wav",
        accept_multiple_files=True,
    )

    samples = []
    for file in files:
        with tempfile.NamedTemporaryFile(
            suffix=".wav",
            prefix=file.name,
            delete=False,
        ) as sample:
            sample.write(file.read())
            samples.append(sample.name)

    container = st.container(horizontal=True)

    run = container.button(
        "Run",
        disabled=not samples,
    )

    if run:
        with st.spinner():
            model = get_model()
            scores = model.predict(samples, activation_layer="softmax")
            scores = scores.idxmax(axis=1)
            scores.name = "label"

            data = io.BytesIO()
            with pd.ExcelWriter(data) as writer:
                scores.to_excel(writer)
                data.seek(0)

    container.download_button(
        "Download",
        data=data if run else "",
        file_name="data.xlsx",
        mime="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        on_click="ignore",
        type="primary",
        disabled=not run,
    )

    for sample in samples:
        os.remove(sample)


@st.cache_resource(show_spinner=False)
def get_model():
    return bmz.BirdNET()


if __name__ == "__main__":
    main()
