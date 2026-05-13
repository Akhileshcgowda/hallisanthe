import urllib.request
import os

downloads = [
    ("onboarding_seller", "https://lh3.googleusercontent.com/aida/ADBb0ug_XS1z1JD23YFZfRzRRlbKAoyeti4rjTmBtj5UX1rvUNDisZ2DKdARV48S1cHcVErEN8c-Da6F6h8_wnrmBQlPqfYm830s9L4vKRYDLdMi_dUSAFuKb6WLHqRdcw12Vlb3uaXlwKjnme1wVnXS3c_ycL3Y2UPhUT_T-lwrYnKsjUue07xVdIP9ef-T3QJHIP0B_Mu6GSd-2UBHJZ0RmwIPFbUbqVZTSkPtQnauvqau1evTogsopj4zyeI", "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ7Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpaCiVodG1sX2YxMWM5ZGQ3N2YxODQwYTNhZDU5NjZiMjE2M2FiM2I0EgsSBxC2oNaD2hYYAZIBIwoKcHJvamVjdF9pZBIVQhM5ODU0MTczMDE3OTY1MTg0NTAx&filename=&opi=89354086"),
    ("login_register_seller", "https://lh3.googleusercontent.com/aida/ADBb0ugewrb5oGO0pPViPczktkqS21n94GYmY4s-GlT0pJ6YLxLrVQe714LabCrhp9u_Dx3y5sOtFSz3911t2hVzHgwH4E7zXIeW-wNJ3rzCp2eUfhVjrT2v2ukCetapdbQg8z0O-bgNrmI3TenLaDC1WRJpSRUY5IQNDCfUYS5Ph3n7sfk6vmwKAmaXI7Wa5iBXkjfAEnOapT0wZ-fi7WkV6nyb6v-z2j7rcYOwMJ_g7QFP0zF3nAdrovqw5w", "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ7Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpaCiVodG1sXzZkMTE5NzQyY2MyMzQzMTdhYmRiYzY4ZmY2NjYxYmQyEgsSBxC2oNaD2hYYAZIBIwoKcHJvamVjdF9pZBIVQhM5ODU0MTczMDE3OTY1MTg0NTAx&filename=&opi=89354086"),
    ("seller_dashboard", "https://lh3.googleusercontent.com/aida/ADBb0uiRWFmWku4wIUKI7Lcj3LX-KwmG1COakrvgvQ7ry2P2pNxE-lpv-gBCB56T3ncLqEcWH0Q17GHnbvE7h66R4N8PDtBUivtjxKrcYD1-oF1BBpUpaqs9FC02uTPcDcDVxc5gRd8t1Vh_dAmgh9-1GU2uIGtIaaNgrdzEMjZnx8OH_aTql7VdOV-Ia3JLFwTMKyGFE28mRV9iOeL1JoSn84LvKj05Ju189YO249lB1GqWZYPxQGDnOCXDlSE", "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ7Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpaCiVodG1sX2JlZDVhOGY2MTYwOTQ5OTI4YzE5YWUwM2RiYTYwZWFhEgsSBxC2oNaD2hYYAZIBIwoKcHJvamVjdF9pZBIVQhM5ODU0MTczMDE3OTY1MTg0NTAx&filename=&opi=89354086"),
    ("add_product", "https://lh3.googleusercontent.com/aida/ADBb0uh3b-gBoCbIHAQa1PX4F3uGAT71i0YLY9TLectQfQ7deEvPyzCS_MWnXfejn3UmEnXOLljDZ1AAKjXgGRcGIUJAbjudp0qB_cF9PdKKQ-tRS8d4P4UGa-3OlJ_NEcKZ9IKt1hS8sIDwva-n8RA5mJBq532k3GutBkIxs9Y9KpB3HfGuHSaiojoZ5uVcJTc8zMZU_SbQecWgXROrUjzbO1WF0s7aI4Fxq5-nwRldsXyIsY3bgCUXBCwOPs4", "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ7Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpaCiVodG1sX2RmM2U4ZTM3MWU1NDQ1NDdiZDdjOWNiNDVlZDM4ZTA1EgsSBxC2oNaD2hYYAZIBIwoKcHJvamVjdF9pZBIVQhM5ODU0MTczMDE3OTY1MTg0NTAx&filename=&opi=89354086"),
    ("my_listings", "https://lh3.googleusercontent.com/aida/ADBb0uizncqg9Daskr0-o08hYQATkIZORCjJFm5eJwxm4d5i8gSe7KPkRlaYmgYQzk4Nv3C8DqJupwRuTWLQ1CFZ8RhMfU0oN4vlP7ODQHMCgzRiRs4jr0dHhWj64S_3VjCm662LQQ0Nsr7vLFenk6kvk2fmvs8ugsCnNG9w3PAwU6PpIsbJTbIffgJynjRNWjqH6-Nocrd1EJbtQxQVgQke_WLLB59Eodirg7zOPaYWlach4nUmcWLwo5ZAig", "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ7Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpaCiVodG1sX2E2ZmUzYWUwYTNkNjQxMDhiMGJhNmVjMmIzMzQ4ZTc3EgsSBxC2oNaD2hYYAZIBIwoKcHJvamVjdF9pZBIVQhM5ODU0MTczMDE3OTY1MTg0NTAx&filename=&opi=89354086"),
    ("inquiries", "https://lh3.googleusercontent.com/aida/ADBb0ugXQyOno48xZ81rDmw6meeRIoBHFxYKvKsjH2CZeEXts7NsLlknpvV-GTibT-q1adlWc6dKj89eaa9TdXIv-J0nCJAp4JN8RCzWl6p9eQ1kNyoJKLBAxFuvYJme2E4_f-Wa4zWt2uUfoQuTI92hrw1dJDp64AUnlzD2_ZZcuAbuL489H-e_yWK8etouV-zn9_NlB5s46-nMoWKvS-DnDI3bmuYziD5Pfc9C3a_yCUg3xYBV5TAyQ02NmQ", "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ7Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpaCiVodG1sXzBmZjkzY2M3NDAwYjRiYzZhZjc1YzA5ODg4ODIyNGZkEgsSBxC2oNaD2hYYAZIBIwoKcHJvamVjdF9pZBIVQhM5ODU0MTczMDE3OTY1MTg0NTAx&filename=&opi=89354086"),
    ("santhe_ai_assistant", "https://lh3.googleusercontent.com/aida/ADBb0uiF5V21oZ5bW_zLvAqfSvR8un6q75mWbrGiVTYN4S7B0I0jUNlWzimFuZkrw0slaSPTZdpY1-gI5UNf83Ae4jITrOsekQepN5H_96Vnkv4FkTIo2RkEJeQaX5SuD_B5NNo1bzt1cy3zHwYYVY8rWH3_2xbeVN3K6Ccp9HHJ9EPm87mnldfO8fy5Oa8q-2X10JPr0qfSzJpKR3EOj6bQsI2POVal0Lo2U42sPeX7odxzIbxpq9YUrT2SSg", "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ7Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpaCiVodG1sXzA2ZjdlZjg0ODI0NDRkOWJiN2UyZTcxNGNmYzc1NTZkEgsSBxC2oNaD2hYYAZIBIwoKcHJvamVjdF9pZBIVQhM5ODU0MTczMDE3OTY1MTg0NTAx&filename=&opi=89354086"),
    ("profile_seller", "https://lh3.googleusercontent.com/aida/ADBb0ugCBiL3g__gDB7fwivBvW6TyAmElJbSoF-2Bbs8ZD73c7J3kDlfABvvSGytYoUQxFGg87Y_XWmlPxuz_irfLS36WjG_SnElzT2yy1N-JfB7ke2AHiP6BmP29DnTGAJ43QDsbr2phdP51D7xHmcOUg1Sr0Ki8uHyCDhyWK69zmZ_xQsZx2_fySRslemX3CAdWlVy_SI802J90sTgVWeImty_d8HOvfbXFj0W4cxqm7BBwTidJBsG3h3axQ", "https://contribution.usercontent.google.com/download?c=CgthaWRhX2NvZGVmeBJ7Eh1hcHBfY29tcGFuaW9uX2dlbmVyYXRlZF9maWxlcxpaCiVodG1sX2U0MzkwZGVhODQxZTQ0NzFiMzlmN2Y5MDdlNDU2ODMxEgsSBxC2oNaD2hYYAZIBIwoKcHJvamVjdF9pZBIVQhM5ODU0MTczMDE3OTY1MTg0NTAx&filename=&opi=89354086")
]

os.makedirs("stitch_screens", exist_ok=True)

for name, img_url, html_url in downloads:
    print(f"Downloading {name}...")
    req_img = urllib.request.Request(img_url, headers={'User-Agent': 'Mozilla/5.0'})
    req_html = urllib.request.Request(html_url, headers={'User-Agent': 'Mozilla/5.0'})
    
    with urllib.request.urlopen(req_img) as response, open(f"stitch_screens/{name}.png", 'wb') as out_file:
        out_file.write(response.read())
        
    with urllib.request.urlopen(req_html) as response, open(f"stitch_screens/{name}.html", 'wb') as out_file:
        out_file.write(response.read())

print("Done")
