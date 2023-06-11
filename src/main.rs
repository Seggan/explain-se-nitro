use std::fs;
use papyri_lang::compile_str;
use papyri_lang::utils::sourcefile::is_papyri_file;

fn main() {
    let lib = fs::read_to_string("chapters/lib.papyri").unwrap();
    for part in fs::read_dir("chapters").unwrap() {
        let part = part.unwrap();
        let path = part.path();
        if !path.is_dir() {
            continue;
        }
        let cloned_path = path.clone();
        let part_num = cloned_path.file_name().unwrap().to_str().unwrap();
        for chapter in fs::read_dir(path).unwrap() {
            let chapter = chapter.unwrap();
            let path = chapter.path();
            if !is_papyri_file(&path) {
                continue;
            }
            let cloned_path = path.clone();
            let chapter_num = cloned_path.file_name()
                .unwrap()
                .to_str()
                .unwrap()
                .split('.')
                .next()
                .unwrap();

            let frame_func = format!(
                r#"
@fn frame $num: int -> {{
    @href("https://se-nitro.surge.sh/\#{}-{}-$num") @image "https://se-nitro.surge.sh/comics/p{}/c{}/$num.png"
}}
                "#,
                part_num,
                chapter_num,
                part_num,
                chapter_num
            );

            let markup = fs::read_to_string(path).unwrap();
            let result = compile_str(format!(
                "{}\n{}\n{}",
                lib.trim(),
                frame_func.trim(),
                markup
            ).as_str());
            match result {
                Ok(result) => {
                    let path = format!("site/data/p{}c{}.html", part_num, chapter_num);
                    fs::write(path, result).unwrap();
                },
                Err(err) => {
                    eprintln!("Error in part {}, chapter {}", part_num, chapter_num);
                    err.print_to_stderr();
                }
            }
        }
    }
}
