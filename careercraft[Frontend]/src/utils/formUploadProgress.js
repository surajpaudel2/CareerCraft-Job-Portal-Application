function formUploadProgress(progressEvent, setProgress) {
  if (progressEvent && progressEvent.total) {
    const progressPercentage = Math.round(
      (progressEvent.loaded * 100) / progressEvent.total
    );
    setProgress(progressPercentage); // Update progress state
  }
}

function startFormUpload(setLoading, setProgress) {
  setLoading(true);
  setProgress(0);
}

function completeFormUpload(setLoading, setProgress) {
  setProgress(100);
  setLoading(false);
  setProgress(0);
}

export { formUploadProgress, startFormUpload, completeFormUpload };
