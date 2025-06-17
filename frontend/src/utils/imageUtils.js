// src/utils/imageUtils.js
export const resizeImage = (file, maxWidth = 800) => {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      
      reader.onload = () => {
        const img = new Image();
        img.src = reader.result;
  
        img.onload = () => {
          const canvas = document.createElement("canvas");
          const scale = maxWidth / img.width;
          const width = maxWidth;
          const height = img.height * scale;
  
          canvas.width = width;
          canvas.height = height;
          const ctx = canvas.getContext("2d");
          ctx.drawImage(img, 0, 0, width, height);
  
          canvas.toBlob((blob) => {
            if (blob) {
              const resizedFile = new File([blob], file.name, {
                type: file.type,
                lastModified: Date.now(),
              });
              resolve(resizedFile);
            } else {
              reject(new Error("리사이징 실패"));
            }
          }, file.type);
        };
      };
  
      reader.onerror = reject;
    });
  };
  