package zip.agil.layar.service;

import org.springframework.stereotype.Service;

@Service
public class SlugService {

    public String toSlug(String text) {
        return text.replaceAll("[^a-zA-Z0-9]", "-");
    }
}
